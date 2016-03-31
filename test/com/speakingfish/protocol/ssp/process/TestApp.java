package com.speakingfish.protocol.ssp.process;

import com.speakingfish.pipe.StreamGobbler;
import com.speakingfish.protocol.ssp.Any;
import com.speakingfish.protocol.ssp.process.SimpleProcessFacade;

import static java.util.Arrays.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.speakingfish.common.debug.DebugHelper.*;
import static com.speakingfish.common.Files.*;
//import static com.speakingfish.protocol.ssp.Helper.*;
import static com.speakingfish.protocol.ssp.xml.Helper.*;

public class TestApp {

    public static void main(String[] args) {
        final List<String> inputFileNames       = new ArrayList<String>();
        final List<String> inputRepeatFileNames = new ArrayList<String>();
        final List<String> processArgs          = new ArrayList<String>();
        
        for(int i = 0; i < args.length; ++i) {
            String param = args[i];
            if("-i".equals(param)) {
                ++i;
                inputFileNames.add(args[i]);
            } else if("-ir".equals(param)) {
                ++i;
                inputRepeatFileNames.add(args[i]);
            } else {
                processArgs.add(args[i]);
            }
        }
        invoke(
            inputFileNames      ,
            inputRepeatFileNames,
            processArgs
            );
    }
    
    public static void invoke(
        final List<String> fileNames, 
        final List<String> inputRepeatFileNames,
        final List<String> processArgs
    ) {
        try {
            //final ProcessBuilder processBuilder = new ProcessBuilder(asList(args).subList(1, args.length));
            final ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
            final Process process = processBuilder.start();
            try {
                final Thread errGobbler = new Thread(new StreamGobbler(process.getErrorStream(), System.err));
                errGobbler.start();
                final SimpleProcessFacade facade = new SimpleProcessFacade(process);
                for(String fileName : fileNames) {
                    final Any<?> command = xmlFileToAny(fileName);
                    final Any<?> result = facade.apply(command);
                    System.out.println(anyToXmlString(result));
                }
                if(0 < inputRepeatFileNames.size()) {
                    while(true) {
                        for(String fileName : inputRepeatFileNames) {
                            final Any<?> command = xmlFileToAny(fileName);
                            final Any<?> result = facade.apply(command);
                            System.out.println(anyToXmlString(result));
                        }
                        Thread.sleep(1000);
                    }
                }
            } finally {
                process.destroy();
            }
        } catch (Exception e) {
            debugError(System.err, "Error.", e);
            System.exit(1);
        }
    }

}
