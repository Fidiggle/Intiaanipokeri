import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
public class IntiaaniPokeri{
  public static void main(String[] args){
    boolean lataa=false;
    System.out.println("---------------------------------INTIAANIPOKERI---------------------------------");
    System.out.println("Kirjoita \"ohje\" jos haluat kuulla säännöt ja kirjoita \"aloita\"");
    System.out.println("jos haluat aloittaa pelin, tai jos haluat aloittaa tallennetun pelin, kirjoita \"lataa\".");
    Scanner s = new Scanner(System.in);
    while(true){ //loop, jotta inputtia voi yrittää uudelleen
      String input= s.nextLine();
      if(input.equals("ohje")){
        System.out.println("Intiaanipokerissa kaikki pelaajat nostavat kortin pakasta ja \"asettavat");
        System.out.println(" sen otsalleen\" niin, että vain muut pelaajat näkevät sen. Tämän jälkeen");
        System.out.println(" pelaajat voivat vuorollaan korottaa panosta muiden korttien perusteella.");
        System.out.println(" Se, kenen kortti on suurin voittaa koko potin. Peliä jatketaan kunnes");
        System.out.println(" kaikilla paitsi voittajalla on alle 5€. Maiden suuruusjärjestys ");
        System.out.println(" suurimmasta pienimpään on Hertta, Ruutu, Pata, Risti. Joka kierroksen ");
        System.out.println(" minimipanos on 5€ ja ässä vastaa ykköstä.");
      }
      else if(input.equals("aloita")){
        break;
      }
      else if(input.equals("lataa")){
        try{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.txt"));
        ois.close();
        } catch(Exception e){
          System.out.println("Tiedostoa ei löytynyt");
          continue;
        }
          lataa=true;
          break;
        }
      else{
        System.out.println("Tunnistamaton komento");
      }
    }
    ArrayList<Pelaaja> pelaajat=new ArrayList<Pelaaja>();//tehdään lista pelaajista
    Pelaaja pelaaja= new Pelaaja();
    Kauppislainen kauppislainen= new Kauppislainen();
    Psykologi psykologi= new Psykologi();
    TilastoTieteilija tilastotieteilija= new TilastoTieteilija();
    pelaajat.add(kauppislainen);
    pelaajat.add(psykologi);
    pelaajat.add(tilastotieteilija);
    pelaajat.add(pelaaja);
    
    if(lataa==false){
      System.out.println("Kuinka paljon aloitusrahaa per pelaaja? (30-250)");
      int rahaa=0;
      while(true){
        try{
          int input= s.nextInt();
          if(30<=input && input<=250){
            rahaa=input;
            break;
          }
          else{
            System.out.println("Epäsopiva määrä");
          }
        }catch(Exception e){
          System.out.println("Ei numero");
          s.next();
        }
      }
      for(Pelaaja p: pelaajat){
        p.asetaRahat(rahaa);
      }
    }
    if(lataa){
      try{
        pelaajat.clear();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.txt"));
        kauppislainen= new Kauppislainen((Kauppislainen)ois.readObject());
        psykologi= new Psykologi((Psykologi)ois.readObject());
        tilastotieteilija= new TilastoTieteilija((TilastoTieteilija)ois.readObject());
        pelaaja= new Pelaaja((Pelaaja)ois.readObject());
        pelaajat.add(kauppislainen);
        pelaajat.add(psykologi);
        pelaajat.add(tilastotieteilija);
        pelaajat.add(pelaaja);
        ois.close();
      }catch(Exception e){
        System.out.println(e);
      }
    }
    System.out.println("Peli alkaa.");
    System.out.println("----------------------------------------------------------------------------");
    peli: //otsikointi ison loopin sulkemiseksi
    while(true){
      for(Iterator<Pelaaja> iterator=pelaajat.iterator(); iterator.hasNext();){//ainut tapa poistaa jotain kesken iteroinnin
        Pelaaja p= iterator.next();
        if(p.kerroRahat()<5 && p.kerroPanos()==0){
          if(p==pelaaja){
            System.out.println("Hävisit pelin!");
            s.close();
            break peli;
          }
          System.out.println(p+" putosi pelistä.");
          System.out.println("");
          iterator.remove();
        }
      }
      for(Pelaaja p: pelaajat){
        if(pelaajat.size()==1 && p==pelaaja){
        System.out.println("Voitit pelin!");
        s.close();
        break peli;
        }
      }
      Korttipakka pakka= new Korttipakka();  
      pakka.sekoitaPakka();
      if(lataa==false){
        for(Pelaaja p: pelaajat){
          p.otaKortti(pakka);
        }
      }
      ArrayList<Pelaaja> pelaajaNakee= new ArrayList<Pelaaja>(pelaajat);
      pelaajaNakee.remove(pelaaja); //tehtiin lista pelin NPCeistä
      int panos=0;
      if (lataa==false){
        panos=5;
      }
      for(Pelaaja p: pelaajat){ //annetaan NPCeille lista muiden korteista
        if(p != pelaaja){
          ArrayList<Pelaaja> tempList= new ArrayList<Pelaaja>(pelaajat);
          tempList.remove(p);
          int[] muidenkortit= new int[tempList.size()];
          int i=0;
          for(Pelaaja pe: tempList){
            muidenkortit[i]=pe.kerroKortti().annaNumero();
            i++;
          }
          p.asetaMuut(muidenkortit);
        }
      }
      if(lataa==false){
        pelaaja.maksaKierros();
        System.out.println("");
        for(Pelaaja p: pelaajaNakee){
          p.maksaKierros();
          p.asetaPanosNyt(panos);
          p.mietiPanos();
          if(p.kerroPanos()!=5){
            p.asetaPanosNyt(p.kerroPanos());
            panos=p.kerroPanos();
          }
          if(p.foldaako()==true){
            System.out.println(p+" foldaa: |"+p.kerroKortti()+"|, asetti vain minimipanoksen: "+ p.kerroPanos()+"€");
          }
          else{
            System.out.println(p +": |"+p.kerroKortti()+"|, asetti panoksen: "+ p.kerroPanos()+"€");
          }
        }
      }
      if(lataa){
        System.out.println("");
        for(Pelaaja p: pelaajaNakee){
          if (p.kerroPanos()>panos){
            panos=p.kerroPanos();
          }
        }
        for(Pelaaja p: pelaajaNakee){
          if(p.foldaako()){
            System.out.println(p+" on foldannut.");
            continue;
          }
          else{
            System.out.println(p+": |"+p.kerroKortti()+"| on asettanut panoksen: "+ p.kerroPanos());
          }
        }
      }
      System.out.println("");
      System.out.println("Foldaa kirjoittamalla \"0\", kirjoita \"1\" vastataksesi suurinta");
      System.out.println("panosta, ja kirjoita \"2\" nähdäksesi paljonko kaikilla pelaajilla on rahaa");
      System.out.println("ja kirjoita \"3\", jos haluat tallentaa.");
      while(true){
        try{
          int input=s.nextInt();
          if(input==2){
            for(Pelaaja p: pelaajat){
              System.out.println(p+": "+p.kerroRahat()+"€");
            }
          }
          else if(input==0){
            pelaaja.foldaa(true);
            System.out.println("Laitoit pöytään vain minimipanoksen "+pelaaja.kerroPanos()+"€ ja foldasit.");
            if(pelaaja.kerroRahat()==0){
              System.out.println("(Niin ei kannata tehdä jos ne 5€ ovat viimeiset rahasi...)");
            }
            break;
          }
          else if(input==1 && panos<=pelaaja.kerroRahat()+pelaaja.kerroPanos()){
            pelaaja.korotaPanos(panos-pelaaja.kerroPanos());
            System.out.println("Laitoit pöytään "+pelaaja.kerroPanos()+"€.");
            if(pelaaja.kerroRahat()==0){
              System.out.println("Olet all-in.");
            }
            break;
          }
          else if(input==1 && panos>pelaaja.kerroRahat()+pelaaja.kerroPanos()){
            System.out.println("Menit all-in lopuilla rahoillasi.");
            pelaaja.korotaPanos(pelaaja.kerroRahat());
            break;
          }
          else if(input==3){
            FileOutputStream out = new FileOutputStream("save.txt");
            ObjectOutputStream ulos = new ObjectOutputStream(out);
            ulos.writeObject(kauppislainen);
            ulos.writeObject(psykologi);
            ulos.writeObject(tilastotieteilija);
            ulos.writeObject(pelaaja);
            ulos.close();
            System.out.println("Peli tallennettu tiedostoon \"save.txt\"");
          }
          else{
            System.out.println("Ei hyväksyttävä numero.");
          }
        }catch(Exception e){
          System.out.println("Ei ole numero.");
          s.next();
        }
     }
     for(Pelaaja p: pelaajaNakee){
       if(p.foldaako()==false && p.kerroPanos()<panos){
         p.asetaPanosNyt(panos);
         p.mietiPanos();
         if(p.foldaako()==true){
          System.out.println(p+" foldaa: |"+p.kerroKortti()+"|, pöydälle aikaisemmin asetettu panos: "+ p.kerroPanos()+"€");
        }
        else{
          if(p.kerroRahat()!=0){
            System.out.println(p +": |"+p.kerroKortti()+"|, pöydälle aikaisemmin asetettu panos: "+ p.kerroPanos()+"€");
          }
        }
       }
     }
     int voittosumma=0;
     for(Pelaaja p: pelaajat){
       voittosumma+=p.kerroPanos();
     }
     ArrayList<Pelikortti> kortit= new ArrayList<Pelikortti>();
     for(Pelaaja p: pelaajat){
       if(p.foldaako()==false){
         kortit.add(p.kerroKortti());
       }
     }
     Pelikortti isoKortti= new Pelikortti(Collections.max(kortit).annaMaa(),Collections.max(kortit).annaNumero());
     Pelaaja voittaja=new Pelaaja();
     for(Pelaaja p: pelaajat){
       if(p.foldaako()==false){
         if(p.kerroKortti().annaMaa().equals(isoKortti.annaMaa()) && p.kerroKortti().annaNumero()==isoKortti.annaNumero()){
           voittaja=p;
         }
       }
     }
     voittaja.asetaRahat(voittaja.kerroRahat()+voittosumma);
     System.out.println("");
     System.out.println("Korttisi oli "+pelaaja.kerroKortti()+", "+voittaja+" voitti "+voittosumma+ "€.");
      for(Pelaaja p: pelaajat){
        p.nollaaPanos();
        p.asetaPanosNyt(0);
        p.foldaa(false);
      }
      lataa=false;
      System.out.println("----------------------------------------------------------------------------");
      System.out.println("");
    }
  }
}