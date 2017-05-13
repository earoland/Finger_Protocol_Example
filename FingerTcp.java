/**
 *Learning about tcp sockets and stuff with java, Finger Dr K edition.
 *
 * @Author Ethan Roland
 *
 * @version 10/1/2016
 *
 */

//needs this to be able to communicate using sockets
import java.net.*;
//needs this to be able to send and recieve data
import java.io.*;

/**
 *overrides java's depricated FingerTcp class
 *will state warning when compiled
 */
public class FingerTcp{

/**
 *Main method of FingerTcp
 *builds up info based on command line and then passes that to another method 
 to do all the hard stuff
 *
 * @param a string of 0 or more command line arguments to accompany this code
 * note: at least hostName is required, the others are optional
 * format: <hostname> [<port>] [<query>]
 */
public static void main(String[] args){
    //holds the hostname, uninitualized because it is required to work
     String hostName;
     //holds the port number, initialized to 0 because of a silly reason in java
     int portNo = 0;
     //holds the query if present, initialized to the default query {C} 
     String userQuery = "\r\n";
    
    //checks to make sure the command line arguments are present and in correct order
    if(args.length == 0){
        System.out.println("usage: finger <hostname> [<port>] [<query>]");
        System.exit(1);
    }else if(args.length == 2){
        try{
            portNo = Integer.parseInt(args[1]);
        }catch(NumberFormatException nfe){
           userQuery = args[1] + userQuery;
        }
    }else{
        if(args.length > 2){
        try{
            portNo = Integer.parseInt(args[1]);
            userQuery = args[2] + userQuery;
        }catch(NumberFormatException nfe){
        System.out.println("usage: finger <hostname> [<port>] [<query>]");
        System.exit(1); 
        }
    }
    }
            hostName = args[0];
            if(portNo == 0){
                portNo = 79;
            }
    //calls make connection to build the socket and transmit/recieve data
    makeConnection(hostName, portNo, userQuery);
}//end of main

/**
 * method to do the dirty work of handling exceptions, building the socket, 
 * and sending/recieving/printing data
 *
 *@param host the hostname to connect to, throws UnknownHostExcption if not found
 *
 *@param port the port to connect to on the host, 79 is default with no cmd args
 *
 * @param query, the query to send to the server based on cmd args,
 * otherwise sends default {C}
 */
public static void makeConnection(String host, int port, String query){
    //try catch block to handle all the exceptions a socket 
    //and sending/recieving data might throw
    try{
        //said socket
        Socket sock = new Socket(host, port);

        // stackoverflow suggested this use to avoid using a scanner
        // and to use the realtively easier println method to send data
        PrintStream sendTo = new PrintStream(sock.getOutputStream());

        //stack overflow suggested to allow use of readLine method 
        //to avoid the need to delimit data on \n
        DataInputStream response = new DataInputStream(sock.getInputStream());
        
        //sending data via println, cool
        sendTo.print(query);
        //reading data via readLine while line isnt null
        String line = response.readLine();
        while(line != null){
            System.out.println(line);
            if(line != null){
            line = response.readLine();
        }}
        //closing the socket, very important method here
        sock.close();

    }
    //trying to catch a malformed socket exception, seems to also catch I/O 
    catch(SocketException se){
       System.out.println("SocketException while trying to connect to " +
       host + ":" + port +
       "\n Connection Refused");
    }
    //trying to catch any bad spelling of hostname
    catch(UnknownHostException uhe){
        System.out.println("UnknownHostException, Double Check Spelling");
    }
    //trying to catch any exception resulting from reading/sending data
    catch(IOException ioe){
       System.out.println("I/O Exception while sending/recieving to " +
       host + ":" + port); 
    }
}
}
