/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import Models.Data;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author huert
 */
public class CEco2 {
    public static void main(String[] args){
        try{
            int pto=1234,max=1500,tp=5;
            InetAddress dir = InetAddress.getByName("127.0.0.1");
            DatagramSocket cl = new DatagramSocket();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String eco_final= "";
            System.out.println("Cliente iniciado en el puerto "+cl.getLocalPort());
            while(true){
                
                System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar.");
                String msj= br.readLine();
                byte[] b = msj.getBytes();
                if(msj.compareToIgnoreCase("salir")==0){
                    System.out.println("Usuario escribio SALIR// Termina aplicacion..");
                    br.close();
                    cl.close();
                    System.exit(0);
                } else if(b.length>max){
                    ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    int np = (int)b.length/tp;
                    int cont = 0;
                    if(b.length%tp>0)
                        np = np+1;
                    for(int i=0;i<np;i++){
                        byte[] b2 = new byte[tp];
                        int n = bais.read(b2);
                        Data d = new Data(i,np,(i-1), b2);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(d);
                        oos.flush();
                        byte[]tmp = baos.toByteArray();
                        System.out.println("tam paquete: "+tmp.length);
                        System.out.println("Se han mandado : "+ (cont+1) + " de " + np + " paquetes esperados");
                        //DatagramPacket p = new DatagramPacket(b2,n,dir,pto);
                        DatagramPacket p = new DatagramPacket(tmp,tmp.length,dir,pto);
                        cl.send(p);
                        DatagramPacket peco = new DatagramPacket(new byte[tmp.length],tmp.length);
                        cl.receive(peco);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(peco.getData()));
                        Data dd = (Data)ois.readObject();
                        String eco = new String(dd.getData(),0,dd.getData().length);
                        cont++;
                        //String eco = new String(peco.getData(),0,peco.getLength());
                        System.out.println("eco: "+eco);
                        eco_final= eco_final + eco;
                        if(i == np-1)
                        {
                             System.out.println("Eco_Final: " + eco_final);
                        }
                        oos.close();
                        ois.close();
                        baos.close();
                    }//for
//                        DatagramPacket peco = new DatagramPacket(new byte[cont*5],cont*5);
//                        cl.receive(peco);
//                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(peco.getData()));
//                        Datos dd = (Datos)ois.readObject();
//                        String eco_final = new String(dd.getdatos(),0,dd.getdatos().length);
//                        System.out.println("eco final: "+eco_final);
//                        ois.close();
                } else{
                        Data d = new Data(0,1,-1, b);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(d);
                        oos.flush();
                        byte[]tmp = baos.toByteArray();
                        //DatagramPacket p = new DatagramPacket(b2,n,dir,pto);
                        DatagramPacket p = new DatagramPacket(tmp,tmp.length,dir,pto);
                        cl.send(p);
                        DatagramPacket peco = new DatagramPacket(new byte[tmp.length],tmp.length);
                        cl.receive(peco);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(peco.getData()));
                        Data dd = (Data)ois.readObject();
                        String eco = new String(dd.getData(),0,dd.getData().length);
                        //String eco = new String(peco.getData(),0,peco.getLength());
                        System.out.println("eco: "+eco);
                        ois.close();
                        oos.close();
                        baos.close();

//                        DatagramPacket p = new DatagramPacket(b,b.length,dir,pto);
//                        cl.send(p);
//                        DatagramPacket peco = new DatagramPacket(new byte[max],max);
//                        cl.receive(peco);
//                        String eco = new String(peco.getData(),0,peco.getLength());
//                        System.out.println("eco: "+eco);
                }//else
            }//while
        }catch(Exception e){
            e.printStackTrace();
        }
    }//main
}
