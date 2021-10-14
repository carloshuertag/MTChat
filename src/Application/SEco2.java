/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import Models.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author huert
 */
public class SEco2 {
    public static void main(String[] args){
        try{
            int pto = 1234,max=65535;
            InetAddress dir = InetAddress.getByName("127.0.0.1");
            String msj_final="";
            String copia ="";
            int seg;
            DatagramSocket s = new DatagramSocket(pto);
                    System.out.println("Servidor de datagrama iniciado en el puerto "+s.getLocalPort());
                    //String msj_final="";
                    while(true){
                        DatagramPacket p = new DatagramPacket(new byte[max],max);
                        s.receive(p);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                        Data dd = (Data)ois.readObject();
                        String msj = new String(dd.getData(),0,dd.getData().length);
                        seg = dd.getPacketNo();
                        copia =msj_final;
                        if(dd.getPacketNo()==0)
                            {
                              System.out.println("-----------------------------------------------------");
                            }
                        msj_final = Mensaje(dd, msj,s, p,msj_final,seg,copia);
                        if(dd.getPacketNo()== dd.getTotal()-1)
                                   {
                                       byte[] b2 = msj_final.getBytes();
                                       Data d = new Data(dd.getPacketNo(),dd.getTotal(),dd.getPrevPacketNo(), b2);
                                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                       ObjectOutputStream oos = new ObjectOutputStream(baos);
                                       oos.writeObject(d);
                                       oos.flush();
                                       p = new DatagramPacket(b2, b2.length,dir,12345);
                                       s.send(p);
                                       System.out.println("Devolviendo eco final...\n");
                                       System.out.println("Mensaje final: " + msj_final);
                                       msj_final="";
                                   }
                    }//while
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
    
    
    private static String Mensaje(Data dd, String msj , DatagramSocket s, DatagramPacket p, String msj_final, 
            int seg,String copia) throws IOException{
       String temp = "";
       int seg_erroneos;
            if(seg>dd.getPrevPacketNo())
            {
               System.out.println("Segmento:"+dd.getPacketNo()+ " de " + (dd.getTotal()-1) + "  Datos recibidos: "+msj+ " Devolviendo eco..");
               s.send(p);
               msj_final = msj_final + msj; 
            }
                else
                {
                    System.out.println("Segmento:"+dd.getPacketNo()+ " de " + (dd.getTotal()-1) + "  Datos recibidos: "+msj+ " Devolviendo eco..");
                    s.send(p);
                    System.out.println("Orden erroneo, corrigiendo mensaje...");
                    seg_erroneos = dd.getPrevPacketNo()- seg;
                    int last_index;
                    last_index = copia.length()- (5*seg_erroneos);
                        //Insert the original string into temp
                    for(int i=0; i<copia.length();i++)
                            {
                                temp += copia.charAt(i);
                                if(i== last_index)
                                {
                                    //Insert the new string in the middle of temp
                                    temp=temp + msj;
                                }
                            }
                    msj_final = temp; 
                }

            //seg = dd.getnp();
            return msj_final;
                 
    }
}
