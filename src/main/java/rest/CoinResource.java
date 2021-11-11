package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CoinOrderDTO;
import dtos.UserDTO;
import facades.CoinFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("coin")

public class CoinResource {

    private static final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private static CoinFacade facade = CoinFacade.getCoinFacade(emf);

    @Context
    private UriInfo context;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;


    @GET
    public String helloCoin(){
        return "coinsite";
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createOrder")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCoinOrder(String coinOrder) {
        String thisuser = securityContext.getUserPrincipal().getName();

        CoinOrderDTO dto = gson.fromJson(coinOrder, CoinOrderDTO.class);
        dto.setUser(thisuser);
        dto = facade.createOrder(dto);
        return gson.toJson(dto);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("orders")
    @RolesAllowed("user")
    public String getAllOrdersByUser(){
        String thisuser = securityContext.getUserPrincipal().getName();
        List<CoinOrderDTO> coinOrderDTOList = facade.getAllordersForUser(thisuser);
        return gson.toJson(coinOrderDTOList);
    }



}
