package shell;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shell.Shell;
import shell.*;


public class MyShell extends Shell{
    LinkedList<String> feladvanyok;
    LinkedList<Character> tippek;
    int hibaszamlalo;
    String feladvany;
    char[] feladat;
    
    public void listaBeolvas(){
      try(Scanner sc = new Scanner(new File("telepules.txt"))){
          while(sc.hasNext()){
              feladvanyok.add(sc.nextLine());
          }
      } catch (FileNotFoundException ex) {
            System.out.println("Hiba a beolvasáskor!");
        }
    }
    
    public boolean talalte(char betu,String miben){
        boolean van = false;
        for (int i = 0; i < miben.length(); i++) {
            if(miben.charAt(i) == betu){
                feladat[i] = betu;
                van = true;
            }
        }
        return van;
    }
    public boolean win(){
        boolean igen = true;
        for (int i = 0; i < feladat.length; i++) {
            if( feladat[i] == '_'){
                igen = false;
            }
        }
    return igen;
    }
    public boolean lose(){
        boolean igen = false;
        for (int i = 0; i < feladat.length; i++) {
            if( feladat[i] == '_'){
                igen = true;
            }
        }
    return igen;
    }
    
    @Override
    protected void init() {
        super.init(); 
        feladvanyok = new LinkedList<>();
        tippek = new LinkedList<>();
        feladvany = null;
        hibaszamlalo = 0;
    }
    
    
    
    public MyShell(){
       init();
       
        addCommand(new Command("load") {
            @Override
            public boolean execute(String... args) {
                if(args.length == 0){
                    System.out.println("Nem adott meg bemenetett!");
                    return false;
                }else{
                    try(Scanner sc = new Scanner(new File(args[0]))){
                        feladvanyok.clear();
                        while (sc.hasNext()) {
                           String sor = sc.nextLine();
                           feladvanyok.add(sor);     
                        }
                    } catch (FileNotFoundException ex) {
                        System.out.println("Hiba a fájl megnyitásakor!");
                        return false;
                    }
                }
                
                return true;
            }
        });
        
        addCommand(new Command("new") {
            @Override
            public boolean execute(String... args) {
                if(args.length > 0){
                    System.out.println("Nem kell paraméter!");
                    return false;
                }else if(feladvanyok.isEmpty()){
                    System.out.println("Üres a feladvany lista!");
                    return false;
                }else{
                    int random = (int) (Math.random()*feladvanyok.size() +0);
                    hibaszamlalo = 0;
                    tippek.clear();
                    feladvany = feladvanyok.get(random);
                    feladat = new char[feladvany.length()];
                    for (int i = 0; i < feladat.length; i++) {
                        feladat[i] = '_';
                    }
                }
                return true;
            }
        });
    
        addCommand(new Command("print") {
            @Override
            public boolean execute(String... args) {
                if(args.length > 0){
                    System.out.println("Nem kell parameter!");
                    return false;
                }else if(feladvanyok.isEmpty()){
                    System.out.println("A feladvanylista ures!");
                    return false;
                }else if(feladvany == null){
                    System.out.println("nincs feladvany kivalasztva!");
                    return false;
                }else if(hibaszamlalo >= 5){
                    System.out.println("Elértük az öt hibát!");
                    if(lose()){
                        System.out.println("Vesztettél a megoldás a következő:");
                        System.out.println(feladvany);
                        feladvany = null;
                        hibaszamlalo = 0;
                        return true;
                    }
                    return false;
                }else{
                    String hangman = null;
                    try {
                       hangman = getHangman(hibaszamlalo);
                    } catch (PhaseNumberOutOfBoundsException ex) {
                        System.out.println("Hiba");
                    }
//                    format("%s", hangman);
                    System.out.println(hangman);
                    for (int i = 0; i < feladat.length; i++) {
                        System.out.print(feladat[i]+" ");
                    }
                    System.out.println();
                    return true;
                }
            }
        });
        
        addCommand(new Command("letter") {
            @Override
            public boolean execute(String... args) {
                if(args.length == 0){
                    System.out.println("Ennek a parancsnak pontosan egy paraméter kell egy betű!");
                    return false;
                }else if(args.length == 1){
                    if(args[0].length() == 1){
                        char betu = Character.toUpperCase(args[0].charAt(0));
                        if(feladvanyok.isEmpty()){
                            System.out.println("A feladvány lista üres!");
                            return false;
                        }else if(feladvany == null){
                            System.out.println("Nincs feladvány kiválasztva!");
                            return false;
                        }else if(!Character.isAlphabetic(betu)){
                            System.out.println("Nem betűt adtál meg!");
                            return false;
                        }else{
                           if(tippek.isEmpty()){
                              tippek.add(betu);
                           }else if(tippek.contains(betu)){
                               System.out.println("Már tippelted!");
                               return false;
                           }else{
                               tippek.add(betu);
                           }
                           
                           if(talalte(betu, feladvany)){
                               System.out.println("Talált!");
                               for (int i = 0; i < feladat.length; i++) {
                                    System.out.print(feladat[i]+" ");
                                }
                                System.out.println();
                                if(win()){
                                    System.out.println("Nyertél!");
                                    feladvany = null;
                                    hibaszamlalo = 0;
                                }
                                return true;
                           }else{
                               hibaszamlalo++;
                                 String hangman = null;
                                     try {
                                        hangman = getHangman(hibaszamlalo);
                                   } catch (PhaseNumberOutOfBoundsException ex) {
                                System.out.println("Hiba");
                            }
                               System.out.println(hangman);
                               if(hibaszamlalo >= 5){
                                System.out.println("Elértük az öt hibát!");
                                if(lose()){
                                System.out.println("Vesztettél a megoldás a következő:");
                                System.out.println(feladvany);
                                feladvany = null;
                                hibaszamlalo = 0;
                                return true;
                                }
                               }
                               System.out.println("Nem talált!");
                               for (int i = 0; i < feladat.length; i++) {
                                System.out.print(feladat[i]+" ");
                                }
                                System.out.println();
                                return true;
                           }
                           
                        }
                    }else{
                        System.out.println("Csak egy karaktert kell megadni!");
                        return false;
                    }
                }
                System.out.println("Csak egy karaktert kell megadni!");
                return false;
            }
        });
        
    }
    
    
    
}
