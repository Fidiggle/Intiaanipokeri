public class Psykologi extends Pelaaja{
  public Psykologi(){}
  public Psykologi(Pelaaja p){
    this.panos=p.kerroPanos();
    this.rahat=p.kerroRahat();
    this.foldaa=p.foldaako();
    this.kortti=p.kerroKortti();
  }
  protected double keskiarvo;
  public void mietiPanos(){
    keskiarvo=0;
    int summa=0;
    int i=0;
    for(int kortti: muidenKortit){
         summa+=kortti;
         i++;
    }
    keskiarvo=(summa/i);
    
    if(this.rahat<=5){
      korotaPanos(this.rahat);
    }
    else if((int)(this.rahat/(13/keskiarvo))<panosNyt && panosNyt>5){
      foldaa(true);
    }
    else{
      korotaPanos((int)(this.rahat/(13/keskiarvo)));
    }
    if(this.rahat==0){
      System.out.println("Psykologi on all-in.");
    }
  }
  public String toString(){
    return "Psykologi";
  }
}