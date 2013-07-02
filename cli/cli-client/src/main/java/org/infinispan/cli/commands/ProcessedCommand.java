package org.infinispan.cli.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessedCommand {
   final String command;
   final String line;
   final List<Argument> arguments;
   final boolean commandComplete;
   final int cursorPosition;
   final int currentArgument;

   public ProcessedCommand(String raw) {
      this(raw, raw.length());
   }

   public ProcessedCommand(String raw, int cursorPosition) {
      this.cursorPosition = cursorPosition;
      this.commandComplete = ltrim(raw).indexOf(' ') > 0;
      raw = raw.trim();
      this.line = raw.endsWith(";") ? raw.substring(0, raw.length()-1) : raw;
      int sep = this.line.indexOf(' ');
      command = sep > 0 ? this.line.substring(0, sep) : this.line;
      arguments = new ArrayList<Argument>();
      splitArguments(sep > 0 ? this.line.substring(sep + 1) : "", sep + 1);
      int c = -1;
      for (Argument arg : arguments) {
         if (cursorPosition>arg.getOffset()) {
            c++;
         } else {
            break;
         }
      }
      this.currentArgument = c;
   }

   private String ltrim(String s) {
      int len = s.length();
      int st = 0;

      while ((st < len) && (s.charAt(st) <= ' ')) {
         st++;
      }
      return st > 0 ? s.substring(st) : s;
   }

   private void splitArguments(String s, int offset) {
      Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
      Matcher regexMatcher = regex.matcher(s);
      while (regexMatcher.find()) {
         if (regexMatcher.group(1) != null) {
            arguments.add(new Parameter(regexMatcher.group(1), offset + regexMatcher.start(1)));
         } else if (regexMatcher.group(2) != null) {
            arguments.add(new Parameter(regexMatcher.group(2), offset + regexMatcher.start(2)));
         } else {
            arguments.add(new Parameter(regexMatcher.group(), offset + regexMatcher.start()));
         }
      }
   }

   public String getCommand() {
      return command;
   }

   public String getCommandLine() {
      return line;
   }

   public List<Argument> getArguments() {
      return arguments;
   }

   public Argument getArgument(int n) {
      return (n >=0 && n < arguments.size()) ? arguments.get(n) : null;
   }

   public Argument getCurrentArgument() {
      return getArgument(currentArgument);
   }

   public boolean isCommandComplete() {
      return commandComplete;
   }
}
