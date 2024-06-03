package it.einjojo.akani.boss.command.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import it.einjojo.akani.boss.room.RoomManager;
import org.bukkit.command.CommandSender;

public class TemplateNameResolver extends ArgumentResolver<CommandSender, TemplateName> {
    private final RoomManager roomManager;

    public TemplateNameResolver(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    protected ParseResult<TemplateName> parse(Invocation<CommandSender> invocation, Argument<TemplateName> argument, String s) {
        return ParseResult.success(new TemplateName(s));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<TemplateName> argument, SuggestionContext context) {
        return SuggestionResult.of(roomManager.roomTemplates().keySet());
    }
}
