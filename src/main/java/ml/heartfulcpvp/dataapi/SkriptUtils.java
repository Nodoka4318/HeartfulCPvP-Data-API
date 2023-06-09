package ml.heartfulcpvp.dataapi;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.variables.Variables;

import javax.annotation.Nullable;

public class SkriptUtils {
    @Nullable
    public static Object getVar(String varName){
        return Variables.getVariable(varName, null, false);
    }

    public static void setVar(String varName, String value){
        Variables.setVariable(varName, value, null, false);
    }
}
