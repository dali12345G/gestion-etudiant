package ressources;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ues")
public class UERessources {

    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UniteEnseignement> getAllUEs() {
        return ueBusiness.getAllUEs();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEByCode(@PathParam("code") int code) {
        UniteEnseignement ue = ueBusiness.getUEByCode(code);
        if (ue != null) {
            return Response.ok(ue).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUE(UniteEnseignement ue) {
        boolean added = ueBusiness.addUE(ue);
        if (added) {
            return Response.status(Response.Status.CREATED).entity(ue).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUE(@PathParam("code") int code, UniteEnseignement ue) {
        boolean updated = ueBusiness.updateUE(code, ue);
        if (updated) {
            return Response.ok(ue).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{code}")
    public Response deleteUE(@PathParam("code") int code) {
        boolean deleted = ueBusiness.deleteUE(code);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
