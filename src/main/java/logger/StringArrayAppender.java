//package logger;
//
//import org.apache.log4j.AppenderSkeleton;
//import org.apache.log4j.spi.LoggingEvent;
//
//import java.util.ArrayDeque;
//import java.util.Collection;
//import java.util.Collections;
//
///**
// * Created by Ihar_Chekan on 5/18/2017.
// */
//public class StringArrayAppender extends AppenderSkeleton {
//
//  private Collection<String> log;
//
//  private int size = 10000;
//
//  public StringArrayAppender() {
//    this.log = new ArrayDeque<String>(size);
//    this.size = size;
//  }
//
//  public String[] getLog() {
//    return log.toArray(new String[0]);
//  }
//
//  @Override
//  protected void append(LoggingEvent event) {
//    // Generate message
//    StringBuilder sb = new StringBuilder();
//    sb.append(event.getTimeStamp()).append(": ");
//    sb.append(event.getLevel().toString()).append(": ");
//    sb.append(event.getRenderedMessage().toString());
//    // add it to queue
//    if(log.size() == size) {
//      ((ArrayDeque<String>) log).removeFirst();
//    }
//    log.add(sb.toString());
//  }
//
//  @Override
//  public void close() {
//    log = Collections.unmodifiableCollection(log);
//  }
//
//  @Override
//  public boolean requiresLayout() {
//    return false;
//  }
//}