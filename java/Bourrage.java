


public class Bourrage{


    public static String bourrageUser(String user){
	int i;
	String bour="";
	if(user.length() >=8){
	    return user.substring(0, 8);
	}
	else{
	    for(i=0; i< (8 - user.length()); i++)
		bour+=' ';	
	    return user+bour;	
	}
    }


    public static String bourrageMachine(String machine){
	int i, j;
	String str="";
	String[] decoup ;
	decoup=machine.split("[.]");

	for (i = 0; i<decoup.length; i++) {
	    if(decoup[i].length()<3){
		for(j=0; j< (3 - decoup[i].length()); j++)
		    str+='0';
		str+=decoup[i];
		decoup[i]=str;
		str="";
	    }
	}
	for (i = 0; i<decoup.length; i++) {
	    str+=decoup[i]+".";
	
	}
	return str.substring(0, str.length()-1);	
    }


    public static String bourragePort(String port){
	int i;
	String str="";
	if(port.length()<5)
	    for(i=0; (i< 5-port.length()); i++ )
		str+='0';
	str+=port;
	return str;

    }
}

