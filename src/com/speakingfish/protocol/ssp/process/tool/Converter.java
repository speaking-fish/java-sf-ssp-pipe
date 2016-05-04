package com.speakingfish.protocol.ssp.process.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import static com.speakingfish.common.Files.*;
import static com.speakingfish.protocol.ssp.Helper.*;
import static com.speakingfish.protocol.ssp.xml.Helper.*;
import static com.speakingfish.protocol.sfp.Helper.*;
import static com.speakingfish.protocol.ssp.yaml.Helper.*;

public class Converter {
    
    public static void usage() {
        System.out.println(""
            + "\nUsage: Converter -i input-file-1 [-i input-file-2 [...]] -o[a] output-file"
            + "\nUse -oa (output append) to append existing file"
            + "\nSupported file extensions: xml, yaml, ssp, sspf"
            + "\nMultiple input file can be used only with sspf output format."
            + "\n"
            );
    }
    
    public static void main(String[] args) {
        List<String> inputFileNames = new ArrayList<String>();
        String outputFileName = null;
        boolean outputAppend = false;
        for(int i = 0; i < args.length; ++i) {
            String param = args[i];
            if("-i".equals(param)) {
                ++i;
                inputFileNames.add(args[i]);
            } else if("-o".equals(param) || "-oa".equals(param)) {
                if(null != outputFileName) {
                    throw new IllegalArgumentException("Only one output file allowed.");
                }
                if("-oa".equals(param)) {
                    outputAppend = false;
                }
                ++i;
                outputFileName = args[i];
            } else if("-h".equals(param)) {
                usage();
                return;
                
            } else {
                throw new IllegalArgumentException("Unknown parameter: " + param);
            }
        }
        if(false
            || (inputFileNames.isEmpty())
            || (null == outputFileName)
        ) {
            usage();
            return;
            
        }
        convert(
            inputFileNames,
            outputFileName,
            outputAppend
            );
    }

    /*
    public static void convert(
        final String inputFileName ,
        final String outputFileName
    ) {
        final String inputFileNameExt  = inputFileName .substring(inputFileName .lastIndexOf('.') + 1);
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
        final String outputFileNameExt = outputFileName.substring(outputFileName.lastIndexOf('.') + 1);
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
    */

    public static void convert(
        final String  inputFileName ,
        final String  outputFileName,
        final boolean outputAppend
    ) {
        final String inputFileNameExt  = inputFileName .substring(inputFileName .lastIndexOf('.') + 1);
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
        final String outputFileNameExt = outputFileName.substring(outputFileName.lastIndexOf('.') + 1);
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
            if(outputAppend) {
                appendAll(new File(outputFileName), frame.toByteArray()); 
            } else {
                writeAll (new File(outputFileName), frame.toByteArray());
            }
        } else {
            throw new IllegalArgumentException("Unsupported format: " + outputFileNameExt);
        }
    }
    
    public static void convert(
        final List<String> inputFileNames,
        final String       outputFileName,
              boolean      outputAppend
    ) {
        final String outputFileNameExt = outputFileName.substring(outputFileName.lastIndexOf('.') + 1);
        if(inputFileNames.size() <= 1) {
        } else if("xml".equalsIgnoreCase(outputFileNameExt)) {
            if(0 < inputFileNames.size() || outputAppend) {
                throw new IllegalArgumentException("Can't concatenate to xml.");
            }
        } else if("yaml".equalsIgnoreCase(outputFileNameExt)) {
            if(0 < inputFileNames.size() || outputAppend) {
                throw new IllegalArgumentException("Can't concatenate to xml.");
            }
        } else if("ssp".equalsIgnoreCase(outputFileNameExt)) {
            if(0 < inputFileNames.size() || outputAppend) {
                throw new IllegalArgumentException("Can't concatenate to ssp.");
            }
        } else if("sspf".equalsIgnoreCase(outputFileNameExt)) {
        } else {
            throw new IllegalArgumentException("Unsupported format: " + outputFileNameExt);
        }
        for(String inputFileName : inputFileNames) {
            convert(inputFileName, outputFileName, outputAppend);
            outputAppend = true;
        }
    }
        
}
