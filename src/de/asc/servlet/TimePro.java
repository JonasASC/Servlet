package de.asc.servlet;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;

import de.asc.exceptions.IllegalRequest;
import de.asc.exceptions.UserNotAuthenticatedException;
import de.asc.ttp.ActiveProjectView;
import de.asc.ttp.Customer;
import de.asc.ttp.DateTime;
import de.asc.ttp.Login;
import de.asc.ttp.Projects;
import de.asc.ttp.Tracker;
import de.asc.ttp.User;
import de.asc.ttp.model.ErrorResponse;
import de.asc.ttp.model.ProjectInfoRequest;

@Path("/")
@Produces("application/json")
public class TimePro {
	private Long userid = null;
	protected Logger logger = Logger.getLogger(TimePro.class.getName());
	

	/**
	 * 
	 * @param req
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	private void initSess(HttpServletRequest req)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		HttpSession session = req.getSession(false);
		if (session != null)
			userid = (Long) session.getAttribute("userid");
			
	}

	@GET
	@Path("/getProjectList")
	public Response getProjectList(@Context HttpServletRequest req) throws IllegalAccessException {
		try {
			initSess(req);

			Projects p = new Projects();
			logger.info("ProjecteList from UserId = " + userid);
			return Response.ok(new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
					.toJson(p.getProjectlistbyeUser(userid))).build();
		} catch (IllegalArgumentException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@GET
	@Path("/getActivelist")
	public Response getActivelist(@Context HttpServletRequest req) throws IllegalAccessException {
		try {
			initSess(req);

			ActiveProjectView a = new ActiveProjectView();
			logger.info("Activelist from UserId " + userid);
			return Response.ok(new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
					.toJson(a.getActiveProjectlistbyeUser(userid))).build();
		} catch (IllegalArgumentException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@POST
	@Path("/addProject")
	@Consumes("application/json")
	public Response addProject(@Context HttpServletRequest req, Projects pro)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		String result = "Projects created : " + pro.getDescription();
		try {
			logger.info("storing pro for user " + userid);
			pro.userid = userid;
			pro.insert();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not save project to database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(pro.toJson()).build();
	}

	@POST
	@Path("/addCustomer")
	@Consumes("application/json")
	public Response addCustomer(@Context HttpServletRequest req, Customer cus)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		
		try {
			logger.info("storing pro for user " + userid);
			cus.userid = userid;
			cus.insert();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not save project to database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(cus.toJson()).build();
	}
	
	
	
	@GET
	@Path("/getProjectInfo/{id}")
	@Consumes("application/json")
	public Response getProjectInfo(@Context HttpServletRequest req, @PathParam("id") Long projectId)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			Projects p = new Projects(projectId);
			if (p.userid != userid)
				throw new IllegalRequest("userids don't match");
			return Response.status(201).entity(p.toJson()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not find ProjectId: " + e.getMessage(), e);
			return Response.status(599).entity(new ErrorResponse(599, e).toJson()).build();
		}
	}
	
	@GET
	@Path("/getTracker/{id}")
	@Consumes("application/json")
	public Response getTracker(@Context HttpServletRequest req, @PathParam("id") Long projectId) 
			throws IllegalAccessException, IllegalArgumentException, SQLException {
		initSess(req);
		try {
			DateTime a = new DateTime();
			logger.info("Tracker from ProjectId " + projectId);
			return Response.ok(new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
					.toJson(a.getDateTimebyeProjectid(projectId))).build();
		} catch (IllegalArgumentException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	@POST
	@Path("/deleteProject")
	@Consumes("application/json")
	public Response deleteProject(@Context HttpServletRequest req, Projects pro)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			pro.delete(pro.id);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could delete from database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(pro.toJson()).build();
	}

	@POST
	@Path("/login")
	@Consumes("application/json")

	public Response login(@Context HttpServletRequest req, Login login) {
		try {
			logger.info(login.email);
			User user = new User(login);
			HttpSession session = req.getSession(true);
			session.setAttribute("userid", user.id);
			return Response.ok().entity(user.toJson()).build();
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			logger.log(Level.SEVERE, "login failed: " + e.getMessage(), e);
			return Response.status(500).entity(e.getMessage()).build();
		} catch (UserNotAuthenticatedException e) {
			return Response.status(403).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("/updateProject")
	@Consumes("application/json")
	public Response updateProject(@Context HttpServletRequest req, Projects pro)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			logger.info("updating Project");
			pro.update();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not update project at database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(pro.toJson()).build();
	}
	
	@POST
	@Path("/updateTimeSlices")
	@Consumes("application/json")
	public Response updateTimeSlices(@Context HttpServletRequest req, DateTime da)
		throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			logger.info("updating TimeSlices");
			da.update();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not update Timeslices at database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(da.toJson()).build();
	}

	@POST
	@Path("/startTs")
	@Consumes("application/json")
	public Response startts(@Context HttpServletRequest req, Tracker track)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			logger.info("Start Time");
			track.StartTs();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not update project at database: " + e.getMessage(), e);
		}
		return Response.status(201).entity(track.toJson()).build();
	}

	@POST
	@Path("/endts")
	@Consumes("application/json")
	public Response endts(@Context HttpServletRequest req, Tracker track)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		initSess(req);
		try {
			logger.info("end Time");
			track.EndTs();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "could not end Projecttime " + e.getMessage(), e);
		}
		return Response.status(201).entity(track.toJson()).build();
	}

}
