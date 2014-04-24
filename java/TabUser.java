


public class TabUser{

    String[] tabUser = new String[100];
    int nbEle;

    public TabUser(){
	this.tabUser = new String[100];
	this.nbEle = 0;
    }


    public  void afficheUserConnect(){
	int i;
	for(i=0; i<nbEle; i++)
	    System.out.println(tabUser[i]);
    }
    

    public void addUser(String user) {
	if(tabUser.length == nbEle){
	    int i;
	    String[] tmp = this.tabUser;
	    tabUser = new String[2*nbEle];
	    for(i=0; i<tmp.length; i++){
		tabUser[i]=tmp[i];
	    }
	    tabUser[i+1]=user;
	    nbEle ++;
	}
	else{
	    tabUser[nbEle] = user;
	    nbEle++;
	}
    }

    public void removeUser(String user){
	int i,j;
	if(nbEle>0){
	    for(i=0; i<nbEle; i++){
		if(user.equals(tabUser[i].substring(4, 12))){
		    // System.out.println("if");
		    nbEle--;
		    break;
		}
	    }
	    for(j=i; j<nbEle; j++){
		System.out.println("if");
		tabUser[j] =tabUser[j+1];
	    }
	}

	else{
	    System.out.println("tabUser vide");
	}
	
    }






    public static void main(String[] args){
	TabUser t = new TabUser();
	t.addUser("HLO safir    192.168.000.014 01234");
	t.addUser("HLO safir    192.168.000.014 01234");
	t.addUser("HLO safir    192.168.000.014 01234");
	t.addUser("HLO safir    192.168.000.014 01234");
	t.addUser("HLO marc    192.168.000.014 01234");

	t.removeUser("marc    ");
	t.afficheUserConnect();

    }


}
