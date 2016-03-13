package com.speakingfish.protocol.ssp.process;

/*
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.speakingfish.protocol.ssp.Any;

import static com.speakingfish.protocol.sfp.Helper.*;
import static com.speakingfish.protocol.ssp.Helper.*;
*/
public class PWProcessFacadeImpl /*implements PWFacade*/ {
//    
////    protected class PWProcessFacadeHandler extends PWProcessFacade {
////
////        public PWProcessFacadeHandler(OutputStream destOutputStream, InputStream processOutputReaderStream) {
////            super(destOutputStream, processOutputReaderStream);
////        }
////
////        @Override public SSPAny execute(SSPAny command) {
////            
////            
////            
////        }
////            /*
////            var frameStream        : TMemoryStream;
////            var outputCommandStream: TMemoryStream;
////            var frame              : TByteDynArray;
////            var waitForData        : boolean;
////            var answer             : ISSPAny;
////            var commandName        : AnsiString;
////            result:= nil;
////            */
////            
////            final ByteArrayOutputStream outputCommandStream = new ByteArrayOutputStream();
////            try {
////                command.writeTo(outputCommandStream);
////                SFPHelper.writeFrame(destOutputStream(), 0, outputCommandStream.size(), outputCommandStream.toByteArray());
////                /*
////                if(_flagOutStd) {
////                    return null;
////                }
////                */
////                
////                while(true) {
////                    final byte[] frame = SFPHelper.readFrame(_processOutputReaderStream/*, @waitForData*/);
////                    /*
////                    if waitForData then
////                      begin
////                        sleep(100);
////                        continue;
////                      end;
////                    */  
////                    if(0 == frame.length) {
////                        /*
////                        answer:= processInvalidPacket(nil);
////                        break;
////                        */
////                        continue;
////                    }
////                    /*
////                    frameStream.reset();
////                    frameStream.write(frame); //[0], frame.length);
////                    frameStream.position:= 0;
////                    */
////                    final ByteArrayInputStream frameStream = new ByteArrayInputStream(frame);
////                    
////                    final SSPAny answer = SSPHelper.readAny(frameStream);
////
////                    final SSPAny commandItem = answer.item(ATTR_COMMAND);
////                    if(null == commandItem) {
////                        invalidPacket(answer);
////                    } else {
////                        final String commandName = commandItem.asUTF8String();
////                        if(COMMAND_RECOGNIZE_LICENCE_PLATE == commandName) {
////                            break;
////                        } else {
////                            return processUnknownCommand(answer);
////                            break;
////                        }
////                }
////            } catch(EOFException e) {
////                throw invalidPacket(null);
////            }
////            /*    
////            } finally {
////                outputCommandStream.free();
////                outputCommandStream:= nil;
////            
////                frameStream.free();
////                frameStream:= nil;
////            }
////            */
////        }
////        
////    }
// 
//    protected final List<String>     _processArgs;
//    
//  //protected PWProcessFacadeHandler _handler;
//    protected Process                _process;
//    
//    protected OutputStream           _destOutputStream;
//    
//    protected boolean                _flagOutStd;
//    
//    public PWProcessFacadeImpl(List<String> processArgs) {
//        super();
//        _processArgs = processArgs;
//    }
///*    
//    protected synchronized PWProcessFacade handler() {
//        if(null == _handler) {
//            _handler = new PWProcessFacadeHandler(process().getOutputStream(), process().getInputStream());
//        }
//        return _handler;
//    }
//*/
//    
//    public boolean flagOutStd() { return _flagOutStd; }
//    
//    public void setFlagOutStd(boolean value) { this._flagOutStd = value; }
//
//    protected OutputStream destOutputStream() {
//        if(_flagOutStd) {
//            if(null == _destOutputStream) {
//                _destOutputStream = System.out;
//            }
//            return _destOutputStream;
//        } else {
//            return process().getOutputStream();
//        }
//    }
//    
//    protected synchronized Process process() {
//        if(null == _process) {
//            final ProcessBuilder builder = new ProcessBuilder(_processArgs);
//
//            try {
//                _process = builder.start();
//            } catch(IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                public void run() {
//                    _process.destroy();
//                }
//                }));
//        }
//        return _process;
//    }
///*
//    @Override public SSPAny execute(SSPAny command) {
//        return handler().execute(command);
//    }
//*/
//    
//    public Any<?> execute(SSPAny command) {
//        /*
//        var frameStream        : TMemoryStream;
//        var outputCommandStream: TMemoryStream;
//        var frame              : TByteDynArray;
//        var waitForData        : boolean;
//        var answer             : ISSPAny;
//        var commandName        : AnsiString;
//        result:= nil;
//        */
//        
//        final ByteArrayOutputStream outputCommandStream = new ByteArrayOutputStream();
//        try {
//            command.writeTo(outputCommandStream);
//            writeFrame(destOutputStream(), 0, outputCommandStream.size(), outputCommandStream.toByteArray());
//            
//            if(_flagOutStd) {
//                return null;
//            }
//            
//            while(true) {
//                final byte[] frame = readFrame(process().getInputStream()/*, @waitForData*/);
//                /*
//                if waitForData then
//                  begin
//                    sleep(100);
//                    continue;
//                  end;
//                */  
//                if(0 == frame.length) {
//                    /*
//                    answer:= processInvalidPacket(nil);
//                    break;
//                    */
//                    continue;
//                }
//                /*
//                frameStream.reset();
//                frameStream.write(frame); //[0], frame.length);
//                frameStream.position:= 0;
//                */
//                final ByteArrayInputStream frameStream = new ByteArrayInputStream(frame);
//                
//                final Any<?> answer = readAny(frameStream);
//
//                final Any<?> commandItem = answer.item(ATTR_COMMAND);
//                if(null == commandItem) {
//                    invalidPacket(answer, null);
//                } else {
//                    final String commandName = commandItem.asString();
//                    if(!handleCommand(commandName, commandItem))
//                        return processUnknownCommand(answer);
//                    }
//                    break;
//                
//                /*
//                command:= answer.item(ATTR_COMMAND);
//                if not assigned(command) then
//                  begin
//                    answer:= processInvalidPacket(answer)
//                  end
//                else
//                  begin
//                    commandName:= command.asUTF8String();
//                    if commandName = COMMAND_RECOGNIZE_LICENCE_PLATE then
//                      begin
//                        break;
//                      end
//                    else
//                      begin
//                        answer:= processUnknownCommand(answer);
//                        break;
//                      end;
//                  end;
//                */
//            }
//        } catch(EOFException e) {
//            throw invalidPacket(null);
//        }
//        /*    
//        } finally {
//            outputCommandStream.free();
//            outputCommandStream:= nil;
//        
//            frameStream.free();
//            frameStream:= nil;
//        }
//        */
//    }
//
//
}
