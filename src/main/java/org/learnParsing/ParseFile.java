package org.learnParsing;

import picocli.CommandLine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "parseFile", mixinStandardHelpOptions = true, description = "Simple POC", version = "1.0")
public class ParseFile implements Callable<Integer> {

    SampleHttpClient shc = new SampleHttpClient();

    BufferedReader reader;
    String path = "/Users/chenya2/Desktop/PicoliParsing/src/main/java/org/learnParsing/";

    @CommandLine.Option(names = "-r", description = "allow printing the file")
    boolean reversable;

    @CommandLine.Option(names = "-f", description = "The input file")
    String inputFile;

    @CommandLine.Option(names = "-o", description = "The output file")
    File outputFile;

    @CommandLine.Option(names = "-w", description = "write to new file")
    boolean writable;

    @CommandLine.Option(names = "-c", description = "write to new file")
    boolean callable;

    List<String> data = new ArrayList<>();

    private void readFile(){
        try{
            reader = new BufferedReader(new FileReader(path + inputFile));
            String line = reader.readLine();
            while (line != null){
                if(!reversable){
                    System.out.println(line);
                    this.data.add(line);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(String s: line.split(" ")){
                        sb.append(s).append(" ");
                    }
                    sb.reverse();
                    this.data.add(sb.toString());
                    System.out.println(sb);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void writeFile(){
        try {
            String fileName = path + outputFile;
            File outFile = new File(fileName);
            if(outFile.createNewFile()){
                System.out.println("File created!");
            }else {
                System.out.println("File already exists");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for(String s: data){
                writer.write(s);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void documentResponse(){
        try {
            String response = path + "response";
            File responseFile = new File(response);
            if(responseFile.createNewFile()){
                System.out.println("File created!");
            }else {
                System.out.println("File already exists");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(response));
            String responseData = shc.SampleCall();
            writer.write(responseData);
            writer.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer call(){
        System.out.println("Pls input sth");
        readFile();
        if (writable){
            writeFile();
        }
        if(callable){
            documentResponse();
        }
        return 0;
    }

    public static void main(String... args) {
        System.exit(new CommandLine(new ParseFile()).execute(args));
    }
}
