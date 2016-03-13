package com.speakingfish.protocol.sfp.process;

import com.speakingfish.pipe.StreamGobbler;
import com.speakingfish.protocol.ssp.Any;
import com.speakingfish.protocol.ssp.process.SimpleProcessFacade;

import static java.util.Arrays.*;

import java.io.File;

import static com.speakingfish.common.debug.DebugHelper.*;
import static com.speakingfish.common.Files.*;
//import static com.speakingfish.protocol.ssp.Helper.*;
import static com.speakingfish.protocol.ssp.xml.Helper.*;

public class TestApp {

    public static void main(String[] args) {
        try {
            final Any<?> command = xmlFileToAny(args[0]);
            final ProcessBuilder processBuilder = new ProcessBuilder(asList(args).subList(1, args.length));
            final Process process = processBuilder.start();
            try {
                final Thread errGobbler = new Thread(new StreamGobbler(process.getErrorStream(), System.err));
                errGobbler.start();
                final SimpleProcessFacade facade = new SimpleProcessFacade(process);
                //while(true) {
                    final Any<?> result = facade.apply(command);
                    System.out.println(anyToXmlString(result));
                //}
            } finally {
                process.destroy();
            }
        } catch (Exception e) {
            debugError(System.err, "Error.", e);
            System.exit(1);
        }
    }

}
