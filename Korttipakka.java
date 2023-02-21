import java.util.ArrayList;
import java.util.Collections;

public class Korttipakka{
  protected ArrayList<Pelikortti> pakka = new ArrayList<Pelikortti>();
  
  public Korttipakka(){
    for(Pelikortti.Maa maa: Pelikortti.Maa.values()){
      for(int i=1; i<=13; i++){
        pakka.add(new Pelikortti(maa,i));
      }
    }
  }
  public void sekoitaPakka(){
    Collections.shuffle(pakka);
    System.out.println("Pakka sekoitettu");
  }
  public Pelikortti nostaKortti(){
    return pakka.remove(0);
  }
  public void resetPakka(){
    pakka.clear();
    for(Pelikortti.Maa maa: Pelikortti.Maa.values()){
      for(int i=1; i<=13; i++){
        pakka.add(new Pelikortti(maa,i));
      }
    }
  }
}
      
    