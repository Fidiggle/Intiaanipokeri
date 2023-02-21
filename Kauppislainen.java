public class Kauppislainen extends Pelaaja{
  public Kauppislainen(){}
  public Kauppislainen(Pelaaja p){
    this.panos=p.kerroPanos();
    this.rahat=p.kerroRahat();
    this.foldaa=p.foldaako();
    this.kortti=p.kerroKortti();
  }
  public void mietiPanos(){
    int kuvat=0;
    for(int kortti: muidenKortit){
      if(kortti>10){
        kuvat++;
      }
    }
    if((this.rahat<=5 || kuvat==0) && panosNyt<this.rahat){
      korotaPanos(this.rahat);
    }
    else if(panosNyt<this.rahat/3 || panosNyt==5){
      korotaPanos(this.rahat/3);
    }
    else if(this.rahat>0){
      foldaa(true);
    }
    if(this.rahat==0){
      System.out.println("Kauppislainen on all-in.");
    }
  }
  
  public String toString(){
    return "Kauppislainen";
  }
}