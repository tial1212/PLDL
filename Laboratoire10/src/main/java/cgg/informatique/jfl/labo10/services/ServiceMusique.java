package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.dao.DAOMusique;
import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListesDeLecture;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;
import java.util.logging.Logger;


@Path("/service/musique")
@Produces("application/json")
public class ServiceMusique {

    @EJB
    private DAOMusique daoMusique;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
	@Path("/createSong")
    @PUT
    public Token createSong(@QueryParam("idToken")    int	  pIdToken,
				            @QueryParam("cle") 	      String  pKey,
                       		@QueryParam("titre")      String  pTitle,
                       		@QueryParam("artiste")    String  pArtist,
                       		@QueryParam("musique")    String  pMusic,
                       		@QueryParam("coverArt")   String  pCoverArt,
                       		@QueryParam("public")     boolean pIsPublic,
                       		@QueryParam("active")     boolean pIsActive) {
    	LOGGER.info("ServiceMusique->createSong("+ pIdToken + "," + pKey +"," + pTitle+ "," + pArtist+ "," + pMusic+ "," + pIsPublic+ "," + pIsActive+")" ); 
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		int idOwner = 0; //TODO get from token in BD
    		return daoMusique.create(idOwner, pTitle, pArtist, pMusic, pCoverArt, pIsPublic, pIsActive);
		}
    	return token;
    }
    
    @Path("/getPrivateSong")
	@PUT
	public Musique getPrivateSong(@QueryParam("idToken")int		pIdToken,
            					  @QueryParam("cle") 	String  pKey ,  
            					  @QueryParam("idSong") int		pIdSong ) {
		LOGGER.info("ServiceMusique->getPrivateSong(" + pIdToken + "," +pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.getPrivateSong(pIdToken,  pIdSong);
	}
    
    @Path("/getPublicSong")
	@PUT
	public Musique getPublicSong(@QueryParam("idToken") int   	pIdToken,
								 @QueryParam("cle") 	String pKey ,  
            			   		 @QueryParam("idSong") 	int    pIdSong ) {
		LOGGER.info("ServiceMusique->getPublicSong("+pIdToken+","+pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.getPublicSong(pIdSong);
	}
    
    
    @Path("/modify")
	@PUT
	public Token modify(@QueryParam("idToken")		int	pIdToken,
            			@QueryParam("cle")		String	pKey,  
            			@QueryParam("idSong")		int		pIdSong, 
                                    @QueryParam("titre")          String	pTitle , 
                                    @QueryParam("artiste")	String	pArtist , 
                                    @QueryParam("vignette")	String	pCoverArt ,
                                    @QueryParam("publique")	boolean	pIsPublic ,
                                    @QueryParam("active") 	boolean	pIsActive) {
		LOGGER.info("ServiceMusique->modify("+pIdToken+","+pKey+","+pIdSong+","+pTitle+","+pArtist+","+pCoverArt+","+pIsPublic+","+pIsActive+")" );
		Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoMusique.modify(pIdSong, pTitle, pArtist, pCoverArt, pIsPublic, pIsActive);
		}
    	return token;
	}
    
    
    @Path("/setActive")
	@PUT
	public Token setActive(@QueryParam("idToken")    int     pIdToken,
            			   @QueryParam("cle") 	    String  pKey ,  
            			   @QueryParam("idSong") 	int     pIdSong ,
            			   @QueryParam("active") 	boolean pActive  ) {
		LOGGER.info("ServiceMusique->setActive(" + pIdToken + "," +pKey+ "," +pIdSong+"," +pActive+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
		if ( token.getEtat() ){
			return daoMusique.setActive(pIdSong, pIdToken, pActive);
		}
    	return token;
	}
    
    @Path("/setPublic")
	@PUT
	public Token setPublic(@QueryParam("idToken")   int     pIdToken,
            			   @QueryParam("cle") 	    String  pKey ,  
            			   @QueryParam("idSong") 	int     pIdSong ,
            			   @QueryParam("publique") 	boolean pIsPublic   ) {
		LOGGER.info("ServiceMusique->setActive(" + pIdToken + "," +pKey+ "," +pIdSong+"," +pIsPublic +")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
		if ( token.getEtat() ){
    		return daoMusique.setPublic(pIdSong, pIdToken, pIsPublic );
		}
    	return token;
	}

	@Path("/getMySongs")
	@PUT
	public List<Musique> getMySongs(@QueryParam("idToken")  int   	pIdToken,
								    @QueryParam("cle") 	    String  pKey,
								    @QueryParam("premier")	int		pFirst,
						            @QueryParam("dernier")	int		pLast) {
		LOGGER.info("ServiceMusique->getMySongs("+pIdToken+ "," +pKey+","+pFirst+","+pLast+")" );
		if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
			return null;
		}
		return daoMusique.getMySongs(pIdToken,pFirst, pLast);
	}

	@Path("/getPublicSongsList")
	@POST
	public List<ListesDeLecture> getPublicSongsList(
		    		@QueryParam( "idToken")	int		pIdToken,
		            @QueryParam("cle")		String	pKey,
		            @QueryParam("premier")	int		pFirst,
		            @QueryParam("dernier")	int		pLast) {
		LOGGER.info("ServiceListeLecture->getPublicSongsList("+pIdToken+","+pKey+","+pFirst+","+pLast+")" );
		Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
	    List<ListesDeLecture> listListesMusiques = null;
		if ( token.getEtat()  ){
	        listListesMusiques = daoMusique.getPublicSongsList(pFirst, pLast);
	}
		return listListesMusiques;
	}
}
