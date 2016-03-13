package com.speakingfish.protocol.ssp.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;

import com.speakingfish.protocol.sfp.InvalidPacketException;
import com.speakingfish.protocol.ssp.Any;

import static com.speakingfish.protocol.sfp.Helper.*;
import static com.speakingfish.protocol.ssp.Helper.*;

public class SimpleProcessFacade implements CommandFacade {

    protected final Process        _process                  ;
    protected final OutputStream   _destOutputStream         ;
    protected final InputStream    _processOutputReaderStream;

    public SimpleProcessFacade(
        Process      process                  ,
        OutputStream destOutputStream         ,
        InputStream  processOutputReaderStream
    ) {
        super();
        _process                   = process                  ;
        _destOutputStream          = destOutputStream         ;
        _processOutputReaderStream = processOutputReaderStream;
    }
    
    public SimpleProcessFacade(Process process) {
        this(process, process.getOutputStream(), process.getInputStream());
    }

    public Any<?> apply(Any<?> command) {
        /*
        var frameStream        : TMemoryStream;
        var outputCommandStream: TMemoryStream;
        var frame              : TByteDynArray;
        var waitForData        : boolean;
        var answer             : ISSPAny;
        var commandName        : AnsiString;
        result:= nil;
        */
        
        final ByteArrayOutputStream outputCommandStream = new ByteArrayOutputStream();
        try {
            command.writeTo(outputCommandStream);
            writeFrame(destOutputStream(), 0, outputCommandStream.size(), outputCommandStream.toByteArray());
            
            /*
            if(_flagOutStd) {
                return null;
            }
            */
            
            while(true) {
                final byte[] frame = readFrame(_processOutputReaderStream/*, @waitForData*/);
                /*
                if waitForData then
                  begin
                    sleep(100);
                    continue;
                  end;
                */  
                if(0 == frame.length) {
                    /*
                    answer:= processInvalidPacket(nil);
                    break;
                    */
                    continue;
                }
                /*
                frameStream.reset();
                frameStream.write(frame); //[0], frame.length);
                frameStream.position:= 0;
                */
                final ByteArrayInputStream frameStream = new ByteArrayInputStream(frame);
                
                return readAny(frameStream);
                /*
                command:= answer.item(ATTR_COMMAND);
                if not assigned(command) then
                  begin
                    answer:= processInvalidPacket(answer)
                  end
                else
                  begin
                    commandName:= command.asUTF8String();
                    if commandName = COMMAND_RECOGNIZE_LICENCE_PLATE then
                      begin
                        break;
                      end
                    else
                      begin
                        answer:= processUnknownCommand(answer);
                        break;
                      end;
                  end;
                */
            }
        } catch(EOFException e) {
            throw invalidPacket(null, e);
        }
        /*    
        } finally {
            outputCommandStream.free();
            outputCommandStream:= nil;
        
            frameStream.free();
            frameStream:= nil;
        }
        */
    }

    protected OutputStream destOutputStream() { return _destOutputStream; }
    
    RuntimeException invalidPacket(Any command, Exception e) {
        if(null == command)
            return new InvalidPacketException("Invalid packet: No data.", e); else
            return new InvalidPacketException("Invalid packet: " + command.toString(), e);
    }

    /*
    SSPAny processUnknownCommand(SSPAny command) {
        throw new IllegalArgumentException("Command unknown: " + command.toString());
    }
    */

}
