package me.ddayo.aris.engine.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.ddayo.aris.Aris
import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.LuaFunc
import me.ddayo.aris.engine.InGameEngine
import me.ddayo.aris.engine.InitEngine
import me.ddayo.aris.engine.wrapper.LuaServerPlayer
import me.ddayo.aris.lua.glue.InitGenerated
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.resources.ResourceLocation


@LuaProvider(InGameEngine.PROVIDER, library = "aris.game.command")
object CommandInGameFunctions {
    /**
     * 명령어를 입력했을때 실행할 함수를 지정합니다.
     * @param of 명령어 id
     * @param func 실행할 함수
     */
    @LuaFunction("register_endpoint")
    fun registerEndpoint(of: String, func: LuaFunc) {
        InGameEngine.INSTANCE!!.commandFunctions[ResourceLocation(Aris.MOD_ID, of)] = func
    }
}

@LuaProvider(InitEngine.PROVIDER, library = "aris.init.command")
object CommandBuilderFunctions {
    /**
     * 하위 커멘드를 추가합니다.
     * @of 추가할 커멘드 이름
     * @return 여기에서 획득한 값을 커멘드 핸들러에 append해야합니다.
     */
    @LuaFunction("sub_command")
    fun subCommand(of: String) = object : AbstractCommandHandler() {
        override fun retrieve() = Commands.literal(of)
        override fun write(ctx: CommandContext<CommandSourceStack>, builder: CommandBuilder) { /* Nothing to write */
        }
    }

    /**
     * 정수 인수를 추가합니다.
     * @of 추가할 정수 인수 이름
     * @return 여기에서 획득한 값을 커멘드 핸들러에 append해야합니다.
     */
    @LuaFunction("integer_arg")
    fun intArg(of: String) = object : AbstractCommandHandler() {
        val rl = ResourceLocation(Aris.MOD_ID, of)
        override fun retrieve() = Commands.argument(of, IntegerArgumentType.integer())
        override fun write(ctx: CommandContext<CommandSourceStack>, builder: CommandBuilder) {
            builder.inner[rl] = IntegerArgumentType.getInteger(ctx, of)
        }
    }

    /**
     * 실수 인수를 추가합니다.
     * @of 추가할 실수 인수 이름
     * @return 여기에서 획득한 값을 커멘드 핸들러에 append해야합니다.
     */
    @LuaFunction("float_arg")
    fun floatArg(of: String) = object : AbstractCommandHandler() {
        val rl = ResourceLocation(Aris.MOD_ID, of)
        override fun retrieve() = Commands.argument(of, DoubleArgumentType.doubleArg())
        override fun write(ctx: CommandContext<CommandSourceStack>, builder: CommandBuilder) {
            builder.inner[rl] = DoubleArgumentType.getDouble(ctx, of)
        }
    }

    val commands = mutableMapOf<ResourceLocation, AbstractCommandHandler>()

    /**
     * 새로운 명령어를 추가합니다.
     * @of 추가할 명령어 이름
     */
    @LuaFunction("create_command")
    fun createCommand(of: String): AbstractCommandHandler {
        val r = object : AbstractCommandHandler() {
            override fun retrieve() = Commands.literal(of)
            override fun write(
                ctx: CommandContext<CommandSourceStack>,
                builder: CommandBuilder
            ) { /* Nothing to write */
            }
        }
        commands[ResourceLocation(Aris.MOD_ID, of)] = r
        return r
    }

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        commands.values.forEach {
            dispatcher.register(it.build() as LiteralArgumentBuilder)
        }
    }
}

@LuaProvider(InitEngine.PROVIDER)
abstract class AbstractCommandHandler : ILuaStaticDecl by InitGenerated.AbstractCommandHandler_LuaGenerated {
    val subCommands = mutableListOf<AbstractCommandHandler>()

    /**
     * 여기에서 설정한 id를 register_endpoint를 통해 등록할 수 있습니다.
     * @param of endpoint id
     */
    @LuaFunction("set_endpoint")
    fun setEndpoint(of: String) {
        endpoint = ResourceLocation(Aris.MOD_ID, of)
    }

    @LuaFunction
    fun append(of: AbstractCommandHandler) {
        subCommands.add(of)
    }

    var endpoint: ResourceLocation? = null
        set(value) {
            if (field != null)
                throw IllegalStateException("Cannot rewrite endpoint")
            field = value
        }

    private var parent: AbstractCommandHandler? = null

    protected abstract fun retrieve(): ArgumentBuilder<CommandSourceStack, *>
    protected abstract fun write(ctx: CommandContext<CommandSourceStack>, builder: CommandBuilder)

    fun build(): ArgumentBuilder<CommandSourceStack, *> {
        val b = retrieve()
        subCommands.forEach {
            it.build(b)
            it.parent = this
        }
        endpoint?.let {
            b.executes {
                execute(it)
                1
            }
        }
        return b
    }

    fun <T : ArgumentBuilder<CommandSourceStack, T>> build(of: ArgumentBuilder<CommandSourceStack, T>) {
        of.then(build())
    }

    fun parse(ctx: CommandContext<CommandSourceStack>, builder: CommandBuilder) {
        write(ctx, builder)
        parent?.parse(ctx, builder)
    }

    fun execute(ctx: CommandContext<CommandSourceStack>) {
        val builder = CommandBuilder()
        parse(ctx, builder)
        InGameEngine.INSTANCE!!.commandFunctions[endpoint
            ?: throw Exception("No endpoint declared to handle this command")]!!.callAsTaskRawArg { task ->
            engine.luaMain.pushNoInline(task.coroutine, ctx.source.player?.let { LuaServerPlayer(it) })
            task.coroutine.newTable()
            for ((rl, act) in builder.inner)
                if (engine.luaMain.pushNoInline(task.coroutine, act) == 1)
                    task.coroutine.setField(-2, rl.path)
            2
        }
    }
}

class CommandBuilder {
    val inner = mutableMapOf<ResourceLocation, Any?>()
}