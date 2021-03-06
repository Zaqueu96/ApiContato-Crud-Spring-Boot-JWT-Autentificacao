package com.apirestjwt.main.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apirestjwt.main.model.ResponseModel;
import com.apirestjwt.main.model.Usuario;
import com.apirestjwt.main.request.JwtRequest;
import com.apirestjwt.main.response.JwtResponse;
import com.apirestjwt.main.security.jwt.JwtTokenUtil;
import com.apirestjwt.main.security.jwt.UserDetailsService;
import com.apirestjwt.main.service.UsuarioService;

@RestController
@CrossOrigin
@RequestMapping(value  = "/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthController extends BaseController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UsuarioService serviceUser;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return this.doResponse(HttpStatus.OK,"Ok pode passar",new JwtResponse(token));
	}
	
	/**
	 * Cadrasto de Usuario
	 * @throws Exception 
	 */
	@PostMapping(path="/register",consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	@ResponseBody
	public ResponseEntity<Object> save(@Valid @RequestBody Usuario user)  {
		ResponseModel response = ResponseModel.getInstance();
		System.out.println(user);
		response.setStatus(HttpStatus.ACCEPTED);
		response.setMensage("Usuario Registrado!");
		response.setData(this.serviceUser.register(user));
		response.setSuccess(true);
		return ResponseEntity.ok(response);
	}

	
	
	/**
	 * Autentificando usuario
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void authenticate(String username, String password) throws Exception,BadCredentialsException {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));		
	}
	
}
