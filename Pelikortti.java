public class Pelikortti implements Comparable<Pelikortti>, java.io.Serializable{
  
  protected enum Maa{
    Hertta(4), Ruutu(3), Pata(2), Risti(1);
    private int arvo;
    Maa(int arvo){
      this.arvo=arvo;
    }
    public int annaArvo(){
      return this.arvo;
    }
  }
  protected Maa maa;
  protected int numero;
  
  public Pelikortti(Maa maa, int numero){
    if(numero>13 || numero<1){
      System.out.println("Eihän toi ees oo kortti");
      return;
    }
    this.maa = maa;
    this.numero = numero;
  }
  public int annaNumero(){
    return this.numero;
  }
  public Maa annaMaa(){
    return this.maa;
  }
  
  public int compareTo(Pelikortti other){
    int temp = Integer.compare(this.annaNumero(), other.annaNumero());
    if(temp!=0){
      return temp;
    }
    else{
      return Integer.compare(this.maa.annaArvo(),other.maa.annaArvo());
    }
  }
  
  public String toString(){
    if(numero==11){
      return maa + " Jätkä";
    }
    if(numero==12){
      return maa + " Rouva";
    }
    if(numero==13){
      return maa + " Kuningas";
    }
    if(numero==1){
      return maa + " Ässä";
    }
    return maa +" "+ numero;
  }
}