package com.speakingfish.protocol.ssp.process.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;

import com.speakingfish.common.exception.wrapped.java.io.WrappedEOFException;
import com.speakingfish.common.exception.wrapped.java.io.WrappedIOException;
import com.speakingfish.protocol.ssp.Any;

import static com.speakingfish.common.Texts.*;
import static com.speakingfish.common.Files.*;
import static com.speakingfish.protocol.ssp.Helper.*;
import static com.speakingfish.protocol.ssp.xml.Helper.*;
import static com.speakingfish.protocol.sfp.Helper.*;
import static com.speakingfish.protocol.ssp.yaml.Helper.*;

public class Converter {
    
    public static void main(String[] args) {
        String inputFileName = null;
        String outputFileName = null;
        for(int i = 0; i < args.length; ++i) {
            String param = args[i];
            if("-i".equals(param)) {
                ++i;
                inputFileName = args[i];
            } else if("-o".equals(param)) {
                ++i;
                outputFileName = args[i];
            }
        }
        convert(
            inputFileName,
            outputFileName
            );
    }

    public static void convert(
        final String inputFileName ,
        final String outputFileName
    ) {
        final String inputFileNameExt  = inputFileName .substring(inputFileName .lastIndexOf('.') + 1);
        final String outputFileNameExt = outputFileName.substring(outputFileName.lastIndexOf('.') + 1);
        Any<?> input = null;
        if(Boolean.FALSE) {
        } else if("xml".equalsIgnoreCase(inputFileNameExt)) {
            input = xmlFileToAny(inputFileName);
        } else if("yaml".equalsIgnoreCase(inputFileNameExt)) {
            input = readYaml(new File(inputFileName));
        } else if("ssp".equalsIgnoreCase(inputFileNameExt)) {
            try {
                input = readAny(readAll(new File(inputFileName)));
            } catch(EOFException e) {
                throw new WrappedEOFException(e);
            }
        } else if("sspf".equalsIgnoreCase(inputFileNameExt)) {
            try {
                input = readAny(readFrame(new ByteArrayInputStream(readAll(new File(inputFileName)))));
            } catch(EOFException e) {
                throw new WrappedEOFException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported format: " + inputFileNameExt);
        }
        if(Boolean.FALSE) {
        } else if("xml".equalsIgnoreCase(outputFileNameExt)) {
            anyToXmlFile(input, outputFileName);
        } else if("yaml".equalsIgnoreCase(outputFileNameExt)) {
            final Node srcAnyToNode = newNode(input);
            //Representer ee = new Representer();
            //ee.getPropertyUtils().
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultScalarStyle(ScalarStyle.LITERAL);
            dumperOptions.setDefaultFlowStyle(FlowStyle.FLOW);
            dumperOptions.setLineBreak(LineBreak.UNIX);
            dumperOptions.setSplitLines(true);
            dumperOptions.setPrettyFlow(true);
            
            
            //System.out.println(new Yaml(dumperOptions).dump(srcAnyToNode));
            
            FileWriter sw;
            try {
                sw = new FileWriter(outputFileName);
            } catch(IOException e) {
                throw new WrappedIOException(e);
            }
            final Emitter e = new Emitter(sw, dumperOptions);
            final Serializer s = new Serializer(e, new Resolver(), dumperOptions, null);
            try {
                s.open();
                s.serialize(srcAnyToNode);
            } catch(IOException e1) {
                throw new WrappedIOException(e1);
            }
        } else if("ssp".equalsIgnoreCase(outputFileNameExt)) {
            writeAll(new File(outputFileName), toBytes(input));
        } else if("sspf".equalsIgnoreCase(outputFileNameExt)) {
            final byte[] buffer = toBytes(input);
            final ByteArrayOutputStream frame = new ByteArrayOutputStream();
            writeFrame(frame, 0, buffer.length, buffer);
            writeAll(new File(outputFileName), frame.toByteArray()); 
        } else {
            throw new IllegalArgumentException("Unsupported format: " + outputFileNameExt);
        }
    }

}
