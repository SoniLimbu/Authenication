package com.login.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ))
                .info(new Info()
                        .title("Login System API - Practice Project for Frontend Developers")
                        .version("1.0.0")
                        .description("""
                            <div style="font-family: 'Segoe UI', sans-serif;">

                            <h1>🎯 Welcome to the Login System API!</h1>
                            <p>This is a <strong>practice backend project</strong> designed for <strong>beginner frontend developers</strong> to learn how to connect a frontend (React, Vue, Angular, or plain HTML/JS) to a real REST API.</p>

                            <hr>

                            <h2>📚 TABLE OF CONTENTS</h2>
                            <ol>
                              <li><a href="#overview">Project Overview</a></li>
                              <li><a href="#setup">Setup & Running</a></li>
                              <li><a href="#roles">User Roles & Permissions</a></li>
                              <li><a href="#auth">Authentication Flow (JWT)</a></li>
                              <li><a href="#frontend">Frontend Integration Guide</a></li>
                              <li><a href="#login-flow">Login Flow - Step by Step</a></li>
                              <li><a href="#register-flow">Registration Flow - Step by Step</a></li>
                              <li><a href="#admin-flow">Admin Flow - Step by Step</a></li>
                              <li><a href="#error-handling">Error Handling</a></li>
                              <li><a href="#cors">CORS Information</a></li>
                              <li><a href="#testing">Testing with Demo Credentials</a></li>
                            </ol>

                            <hr>

                            <h2 id="overview">1️⃣ PROJECT OVERVIEW</h2>
                            <p>This Spring Boot backend provides a complete authentication and user management system with <strong>3 user roles</strong>.</p>

                            <h3>What you can build with this API:</h3>
                            <ul>
                              <li>✅ <strong>Login Page</strong> - Let users sign in with email + password</li>
                              <li>✅ <strong>Registration Page</strong> - Let new customers create accounts</li>
                              <li>✅ <strong>Admin Dashboard</strong> - Admin can manage all users, create staff, change roles</li>
                              <li>✅ <strong>Staff Dashboard</strong> - Staff can view their profile</li>
                              <li>✅ <strong>Customer Dashboard</strong> - Customers can view their profile</li>
                              <li>✅ <strong>Role-Based Access</strong> - Different pages for different users</li>
                            </ul>

                            <hr>

                            <h2 id="setup">2️⃣ SETUP & RUNNING</h2>

                            <h3>Prerequisites:</h3>
                            <ul>
                              <li>Java 17+ installed</li>
                              <li>PostgreSQL installed with database named <code>login</code></li>
                              <li>Maven installed</li>
                            </ul>

                            <h3>How to run the backend:</h3>
                            <pre style="background:#f5f5f5;padding:15px;border-radius:5px;border-left:4px solid #4CAF50;">
                # Step 1: Create the PostgreSQL database
                psql -U postgres -c "CREATE DATABASE login;"

                # Step 2: Start the backend
                mvn spring-boot:run

                # Step 3: Insert the admin user manually (in a NEW terminal)
                psql -U postgres -d login -f init.sql
                            </pre>

                            <p>Once running, the API is available at: <code>http://localhost:8080</code></p>
                            <p>Swagger UI is available at: <a href="http://localhost:8080/swagger-ui.html" target="_blank">http://localhost:8080/swagger-ui.html</a></p>

                            <hr>

                            <h2 id="roles">3️⃣ USER ROLES & PERMISSIONS</h2>

                            <table border="1" cellpadding="8" cellspacing="0" style="border-collapse:collapse;width:100%;">
                              <tr style="background:#4CAF50;color:white;">
                                <th>Role</th>
                                <th>Can Access</th>
                                <th>Created By</th>
                              </tr>
                              <tr>
                                <td><strong>ADMIN</strong></td>
                                <td>All endpoints</td>
                                <td>Inserted manually via SQL (init.sql)</td>
                              </tr>
                              <tr>
                                <td><strong>STAFF</strong></td>
                                <td>Staff profile, Customer profile</td>
                                <td>Created by ADMIN via the API</td>
                              </tr>
                              <tr>
                                <td><strong>CUSTOMER</strong></td>
                                <td>Own profile only</td>
                                <td>Self-registered via the API</td>
                              </tr>
                            </table>

                            <h3>Endpoint Access by Role:</h3>
                            <table border="1" cellpadding="6" cellspacing="0" style="border-collapse:collapse;width:100%;">
                              <tr style="background:#2196F3;color:white;">
                                <th>Endpoint</th>
                                <th>Public</th>
                                <th>ADMIN</th>
                                <th>STAFF</th>
                                <th>CUSTOMER</th>
                              </tr>
                              <tr><td>POST /api/auth/login</td><td>✅</td><td>✅</td><td>✅</td><td>✅</td></tr>
                              <tr><td>POST /api/auth/register</td><td>✅</td><td>✅</td><td>✅</td><td>✅</td></tr>
                              <tr><td>POST /api/admin/staff</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>GET /api/admin/users</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>GET /api/admin/users/{id}</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>PUT /api/admin/users/{id}/role</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>PUT /api/admin/users/{id}/status</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>DELETE /api/admin/users/{id}</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>GET /api/admin/stats</td><td>❌</td><td>✅</td><td>❌</td><td>❌</td></tr>
                              <tr><td>GET /api/staff/profile</td><td>❌</td><td>✅</td><td>✅</td><td>❌</td></tr>
                              <tr><td>GET /api/customer/profile</td><td>❌</td><td>✅</td><td>✅</td><td>✅</td></tr>
                            </table>

                            <hr>

                            <h2 id="auth">4️⃣ AUTHENTICATION FLOW (JWT)</h2>

                            <p>This API uses <strong>JSON Web Tokens (JWT)</strong> for authentication. Here's how it works:</p>

                            <h3>Step-by-step JWT Flow:</h3>
                            <ol>
                              <li><strong>User submits credentials</strong> - Frontend sends email + password to <code>POST /api/auth/login</code></li>
                              <li><strong>Server validates</strong> - Backend checks if email exists and password matches</li>
                              <li><strong>Server creates JWT</strong> - A signed token is generated containing the user's email and role</li>
                              <li><strong>Server returns token</strong> - The JWT token + user info is sent back to the frontend</li>
                              <li><strong>Frontend stores token</strong> - Save the token in localStorage or sessionStorage</li>
                              <li><strong>Frontend sends token</strong> - For every subsequent request, include the token in the HTTP header:
                                <pre style="background:#f5f5f5;padding:10px;border-radius:5px;">
                Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
                                </pre>
                              </li>
                              <li><strong>Server validates token</strong> - Backend checks if the token is valid and not expired</li>
                              <li><strong>Access granted/denied</strong> - Based on the user's role in the token</li>
                            </ol>

                            <h3>Token Details:</h3>
                            <ul>
                              <li>Tokens expire after <strong>24 hours</strong> (configurable)</li>
                              <li>When expired, the user must log in again</li>
                              <li>Store the token securely (never expose in URLs)</li>
                            </ul>

                            <hr>

                            <h2 id="frontend">5️⃣ FRONTEND INTEGRATION GUIDE</h2>

                            <p>Here is how to connect from different frontend technologies:</p>

                            <h3>🌐 JavaScript / React (using fetch)</h3>
                            <pre style="background:#1e1e1e;color:#d4d4d4;padding:15px;border-radius:5px;overflow-x:auto;">
                <span style="color:#569cd6;">// === LOGIN ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">login</span>(email, password) {
                  <span style="color:#569cd6;">const</span> response = <span style="color:#dcdcaa;">await</span> <span style="color:#dcdcaa;">fetch</span>(<span style="color:#ce9178;">'http://localhost:8080/api/auth/login'</span>, {
                    method: <span style="color:#ce9178;">'POST'</span>,
                    headers: { <span style="color:#ce9178;">'Content-Type'</span>: <span style="color:#ce9178;">'application/json'</span> },
                    body: <span style="color:#dcdcaa;">JSON.stringify</span>({ email, password })
                  });
                  <span style="color:#569cd6;">const</span> result = <span style="color:#dcdcaa;">await</span> response.<span style="color:#dcdcaa;">json</span>();

                  <span style="color:#6a9955;">// Save token in localStorage</span>
                  <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">setItem</span>(<span style="color:#ce9178;">'token'</span>, result.data.token);
                  <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">setItem</span>(<span style="color:#ce9178;">'user'</span>, <span style="color:#dcdcaa;">JSON.stringify</span>(result.data));

                  <span style="color:#569cd6;">return</span> result.data; <span style="color:#6a9955;">// Contains: token, id, email, username, role</span>
                }

                <span style="color:#569cd6;">// === AUTHENTICATED REQUEST ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">getUsers</span>() {
                  <span style="color:#569cd6;">const</span> token = <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">getItem</span>(<span style="color:#ce9178;">'token'</span>);
                  <span style="color:#569cd6;">const</span> response = <span style="color:#dcdcaa;">await</span> <span style="color:#dcdcaa;">fetch</span>(<span style="color:#ce9178;">'http://localhost:8080/api/admin/users'</span>, {
                    headers: {
                      <span style="color:#ce9178;">'Authorization'</span>: <span style="color:#ce9178;">`Bearer ${token}`</span>,
                      <span style="color:#ce9178;">'Content-Type'</span>: <span style="color:#ce9178;">'application/json'</span>
                    }
                  });
                  <span style="color:#569cd6;">return</span> <span style="color:#dcdcaa;">await</span> response.<span style="color:#dcdcaa;">json</span>();
                }

                <span style="color:#569cd6;">// === REGISTER ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">register</span>(email, password, confirmPassword) {
                  <span style="color:#569cd6;">const</span> response = <span style="color:#dcdcaa;">await</span> <span style="color:#dcdcaa;">fetch</span>(<span style="color:#ce9178;">'http://localhost:8080/api/auth/register'</span>, {
                    method: <span style="color:#ce9178;">'POST'</span>,
                    headers: { <span style="color:#ce9178;">'Content-Type'</span>: <span style="color:#ce9178;">'application/json'</span> },
                    body: <span style="color:#dcdcaa;">JSON.stringify</span>({ email, password, confirmPassword })
                  });
                  <span style="color:#569cd6;">return</span> <span style="color:#dcdcaa;">await</span> response.<span style="color:#dcdcaa;">json</span>();
                }

                <span style="color:#569cd6;">// === CHECK USER ROLE (after login) ===</span>
                <span style="color:#569cd6;">function</span> <span style="color:#dcdcaa;">getUserRole</span>() {
                  <span style="color:#569cd6;">const</span> userStr = <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">getItem</span>(<span style="color:#ce9178;">'user'</span>);
                  <span style="color:#569cd6;">if</span> (!userStr) <span style="color:#569cd6;">return</span> <span style="color:#569cd6;">null</span>;
                  <span style="color:#569cd6;">return</span> <span style="color:#dcdcaa;">JSON.parse</span>(userStr).role; <span style="color:#6a9955;">// 'ADMIN', 'STAFF', or 'CUSTOMER'</span>
                }

                <span style="color:#569cd6;">// === REDIRECT BASED ON ROLE (after login) ===</span>
                <span style="color:#569cd6;">function</span> <span style="color:#dcdcaa;">redirectToDashboard</span>(user) {
                  <span style="color:#569cd6;">switch</span>(user.role) {
                    <span style="color:#569cd6;">case</span> <span style="color:#ce9178;">'ADMIN'</span>: <span style="color:#dcdcaa;">window.location.href</span> = <span style="color:#ce9178;">'/admin-dashboard'</span>; <span style="color:#569cd6;">break</span>;
                    <span style="color:#569cd6;">case</span> <span style="color:#ce9178;">'STAFF'</span>: <span style="color:#dcdcaa;">window.location.href</span> = <span style="color:#ce9178;">'/staff-dashboard'</span>; <span style="color:#569cd6;">break</span>;
                    <span style="color:#569cd6;">case</span> <span style="color:#ce9178;">'CUSTOMER'</span>: <span style="color:#dcdcaa;">window.location.href</span> = <span style="color:#ce9178;">'/customer-dashboard'</span>; <span style="color:#569cd6;">break</span>;
                  }
                }
                            </pre>

                            <h3>⚛️ React Example (using Axios)</h3>
                            <pre style="background:#1e1e1e;color:#d4d4d4;padding:15px;border-radius:5px;overflow-x:auto;">
                <span style="color:#6a9955;">// Install: npm install axios</span>
                <span style="color:#569cd6;">import</span> axios <span style="color:#569cd6;">from</span> <span style="color:#ce9178;">'axios'</span>;

                <span style="color:#6a9955;">// Create axios instance with base config</span>
                <span style="color:#569cd6;">const</span> api = axios.<span style="color:#dcdcaa;">create</span>({
                  baseURL: <span style="color:#ce9178;">'http://localhost:8080/api'</span>,
                  headers: { <span style="color:#ce9178;">'Content-Type'</span>: <span style="color:#ce9178;">'application/json'</span> }
                });

                <span style="color:#6a9955;">// Add auth token to every request automatically</span>
                api.interceptors.request.use(<span style="color:#dcdcaa;">config</span> => {
                  <span style="color:#569cd6;">const</span> token = <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">getItem</span>(<span style="color:#ce9178;">'token'</span>);
                  <span style="color:#569cd6;">if</span> (token) {
                    config.headers.Authorization = <span style="color:#ce9178;">`Bearer ${token}`</span>;
                  }
                  <span style="color:#569cd6;">return</span> config;
                });

                <span style="color:#6a9955;">// === LOGIN ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">login</span>(email, password) {
                  <span style="color:#569cd6;">try</span> {
                    <span style="color:#569cd6;">const</span> { data } = <span style="color:#569cd6;">await</span> api.<span style="color:#dcdcaa;">post</span>(<span style="color:#ce9178;">'/auth/login'</span>, { email, password });
                    <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">setItem</span>(<span style="color:#ce9178;">'token'</span>, data.data.token);
                    <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">setItem</span>(<span style="color:#ce9178;">'user'</span>, <span style="color:#dcdcaa;">JSON.stringify</span>(data.data));
                    <span style="color:#569cd6;">return</span> data.data;
                  } <span style="color:#569cd6;">catch</span> (error) {
                    <span style="color:#569cd6;">throw</span> error.response?.data?.message || <span style="color:#ce9178;">'Login failed'</span>;
                  }
                }

                <span style="color:#6a9955;">// === REGISTER ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">register</span>(email, password, confirmPassword) {
                  <span style="color:#569cd6;">const</span> { data } = <span style="color:#569cd6;">await</span> api.<span style="color:#dcdcaa;">post</span>(<span style="color:#ce9178;">'/auth/register'</span>, { email, password, confirmPassword });
                  <span style="color:#569cd6;">return</span> data;
                }

                <span style="color:#6a9955;">// === ADMIN: GET ALL USERS ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">getAllUsers</span>() {
                  <span style="color:#569cd6;">const</span> { data } = <span style="color:#569cd6;">await</span> api.<span style="color:#dcdcaa;">get</span>(<span style="color:#ce9178;">'/admin/users'</span>);
                  <span style="color:#569cd6;">return</span> data.data; <span style="color:#6a9955;">// Array of user objects</span>
                }

                <span style="color:#6a9955;">// === ADMIN: CREATE STAFF ===</span>
                <span style="color:#569cd6;">async function</span> <span style="color:#dcdcaa;">createStaff</span>(staffData) {
                  <span style="color:#569cd6;">const</span> { data } = <span style="color:#569cd6;">await</span> api.<span style="color:#dcdcaa;">post</span>(<span style="color:#ce9178;">'/admin/staff'</span>, staffData);
                  <span style="color:#569cd6;">return</span> data;
                }

                <span style="color:#6a9955;">// === LOGOUT ===</span>
                <span style="color:#569cd6;">function</span> <span style="color:#dcdcaa;">logout</span>() {
                  <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">removeItem</span>(<span style="color:#ce9178;">'token'</span>);
                  <span style="color:#dcdcaa;">localStorage</span>.<span style="color:#dcdcaa;">removeItem</span>(<span style="color:#ce9178;">'user'</span>);
                  <span style="color:#dcdcaa;">window.location.href</span> = <span style="color:#ce9178;">'/login'</span>;
                }
                            </pre>

                            <hr>

                            <h2 id="login-flow">6️⃣ LOGIN FLOW - STEP BY STEP</h2>

                            <h3>Frontend Steps:</h3>
                            <ol>
                              <li><strong>Create a Login form</strong> with email and password inputs</li>
                              <li><strong>User clicks "Sign In"</strong> → frontend calls <code>POST /api/auth/login</code></li>
                              <li><strong>Show loading state</strong> while waiting for response</li>
                              <li><strong>On success (200):</strong>
                                <ul>
                                  <li>Extract <code>data.token</code> from the response</li>
                                  <li>Save token to <code>localStorage.setItem('token', data.data.token)</code></li>
                                  <li>Save user info to <code>localStorage.setItem('user', JSON.stringify(data.data))</code></li>
                                  <li>Check <code>data.data.role</code> to determine which dashboard to show</li>
                                  <li>Redirect user to the correct dashboard (ADMIN → /admin, STAFF → /staff, CUSTOMER → /customer)</li>
                                </ul>
                              </li>
                              <li><strong>On error (401):</strong>
                                <ul>
                                  <li>Show error message: <code>"Invalid email or password"</code></li>
                                  <li>Let user try again</li>
                                </ul>
                              </li>
                            </ol>

                            <h3>Example Response (Success):</h3>
                            <pre style="background:#f5f5f5;padding:10px;border-radius:5px;">
                {
                  "success": true,
                  "message": "Login successful",
                  "data": {
                    "token": "eyJhbGciOiJIUzI1NiJ9...",
                    "type": "Bearer",
                    "id": 1,
                    "email": "admin@system.com",
                    "username": "admin",
                    "fullName": "System Administrator",
                    "role": "ADMIN"
                  },
                  "timestamp": "2026-06-23T12:00:00"
                }
                            </pre>

                            <h3>Example Response (Error):</h3>
                            <pre style="background:#f5f5f5;padding:10px;border-radius:5px;">
                {
                  "success": false,
                  "message": "Invalid email or password",
                  "data": null,
                  "timestamp": "2026-06-23T12:00:00"
                }
                            </pre>

                            <hr>

                            <h2 id="register-flow">7️⃣ REGISTRATION FLOW - STEP BY STEP</h2>

                            <h3>Frontend Steps:</h3>
                            <ol>
                              <li><strong>Create a Registration form</strong> with email, password, confirm password inputs</li>
                              <li><strong>Validate on frontend:</strong>
                                <ul>
                                  <li>Email looks valid (contains @ and .)</li>
                                  <li>Password is at least 6 characters</li>
                                  <li>Password and confirm password match</li>
                                </ul>
                              </li>
                              <li><strong>User clicks "Register"</strong> → frontend calls <code>POST /api/auth/register</code></li>
                              <li><strong>On success (201):</strong>
                                <ul>
                                  <li>Show success message: "Registration successful! You can now login."</li>
                                  <li>Redirect to Login page</li>
                                </ul>
                              </li>
                              <li><strong>On error (400):</strong>
                                <ul>
                                  <li>Show the error message (e.g., "Email is already registered" or "Passwords do not match")</li>
                                </ul>
                              </li>
                              <li><strong>Note:</strong> Username is auto-generated from the email prefix (e.g., john@email.com → username: "john")</li>
                            </ol>

                            <hr>

                            <h2 id="admin-flow">8️⃣ ADMIN FLOW - STEP BY STEP</h2>

                            <h3>Admin login first, then:</h3>

                            <h4>A. View All Users</h4>
                            <ol>
                              <li>Call <code>GET /api/admin/users</code> with Admin token</li>
                              <li>Display the list in a table showing: id, username, email, role, status, created date</li>
                              <li>Each row can have action buttons (Change Role, Enable/Disable, Delete)</li>
                            </ol>

                            <h4>B. Create a Staff Member</h4>
                            <ol>
                              <li>Show a form with: username, email, password, confirm password, full name</li>
                              <li>Call <code>POST /api/admin/staff</code> with Admin token</li>
                              <li>On success, show the new staff user and a success message</li>
                              <li>The new staff can now log in with their credentials</li>
                            </ol>

                            <h4>C. Change User Role</h4>
                            <ol>
                              <li>Select a user and choose a new role from dropdown (ADMIN, STAFF, CUSTOMER)</li>
                              <li>Call <code>PUT /api/admin/users/{id}/role?role=STAFF</code></li>
                              <li>The user's permissions change immediately</li>
                            </ol>

                            <h4>D. Enable/Disable User</h4>
                            <ol>
                              <li>Call <code>PUT /api/admin/users/{id}/status</code></li>
                              <li>Disabled users <strong>cannot log in</strong></li>
                              <li>Re-enable them to restore access</li>
                            </ol>

                            <h4>E. Delete User</h4>
                            <ol>
                              <li>Call <code>DELETE /api/admin/users/{id}</code></li>
                              <li>⚠️ This permanently removes the user from the database</li>
                              <li>Consider disabling instead of deleting</li>
                            </ol>

                            <hr>

                            <h2 id="error-handling">9️⃣ ERROR HANDLING</h2>

                            <p>All API responses follow a consistent format:</p>
                            <pre style="background:#f5f5f5;padding:10px;border-radius:5px;">
                {
                  "success": true/false,      // Whether the request succeeded
                  "message": "Some message",  // Human-readable result message
                  "data": { ... },            // The actual data (or null on error)
                  "timestamp": "2026-06-23T12:00:00"  // When the response was sent
                }
                            </pre>

                            <h3>Common HTTP Status Codes:</h3>
                            <table border="1" cellpadding="6" cellspacing="0" style="border-collapse:collapse;width:100%;">
                              <tr style="background:#FF9800;color:white;">
                                <th>Status Code</th>
                                <th>Meaning</th>
                                <th>What to do in Frontend</th>
                              </tr>
                              <tr><td><strong>200 OK</strong></td><td>Request succeeded</td><td>Process the data</td></tr>
                              <tr><td><strong>201 Created</strong></td><td>Resource created successfully</td><td>Show success & redirect</td></tr>
                              <tr><td><strong>400 Bad Request</strong></td><td>Invalid input (validation error)</td><td>Show error message to user</td></tr>
                              <tr><td><strong>401 Unauthorized</strong></td><td>Invalid email/password or expired token</td><td>Redirect to login page</td></tr>
                              <tr><td><strong>403 Forbidden</strong></td><td>Valid token but insufficient role</td><td>Show "Access Denied" page</td></tr>
                              <tr><td><strong>404 Not Found</strong></td><td>Resource not found</td><td>Show "Not Found" message</td></tr>
                              <tr><td><strong>500 Internal Server Error</strong></td><td>Server error</td><td>Show "Something went wrong"</td></tr>
                            </table>

                            <hr>

                            <h2 id="cors">🔟 CORS INFORMATION</h2>

                            <p>CORS (Cross-Origin Resource Sharing) is already configured on the backend. This means:</p>
                            <ul>
                              <li>✅ <strong>Any frontend domain</strong> can call this API (React on localhost:3000, Vue on localhost:5173, etc.)</li>
                              <li>✅ <strong>All common HTTP methods</strong> are allowed (GET, POST, PUT, DELETE, PATCH, OPTIONS)</li>
                              <li>✅ <strong>The Authorization header</strong> is exposed so your frontend can read it</li>
                              <li>❌ <strong>Credentials/cookies</strong> are NOT required since we use JWT tokens</li>
                            </ul>

                            <p>If you're using <strong>React</strong> with <strong>fetch</strong> on a different port, it will work automatically. No proxy configuration needed!</p>

                            <hr>

                            <h2 id="testing">1️⃣1️⃣ TESTING WITH DEMO CREDENTIALS</h2>

                            <p>After running <code>init.sql</code>, you have these test accounts:</p>
                            <table border="1" cellpadding="6" cellspacing="0" style="border-collapse:collapse;width:100%;">
                              <tr style="background:#4CAF50;color:white;">
                                <th>Role</th>
                                <th>Email</th>
                                <th>Password</th>
                              </tr>
                              <tr><td><strong>ADMIN</strong></td><td>admin@system.com</td><td>admin123</td></tr>
                            </table>

                            <p>For STAFF and CUSTOMER accounts, you can:</p>
                            <ol>
                              <li>Login as ADMIN → use <strong>POST /api/admin/staff</strong> to create staff</li>
                              <li>Or use <strong>POST /api/auth/register</strong> to register as a customer</li>
                            </ol>

                            <h3>Quick Test with curl:</h3>
                            <pre style="background:#1e1e1e;color:#d4d4d4;padding:15px;border-radius:5px;">
                <span style="color:#6a9955;"># Login as admin</span>
                curl -X POST http://localhost:8080/api/auth/login \\
                  -H "Content-Type: application/json" \\
                  -d '{"email":"admin@system.com","password":"admin123"}'

                <span style="color:#6a9955;"># Create a staff member (replace TOKEN with the token from login)</span>
                curl -X POST http://localhost:8080/api/admin/staff \\
                  -H "Content-Type: application/json" \\
                  -H "Authorization: Bearer TOKEN" \\
                  -d '{"username":"staff1","email":"staff1@test.com","password":"staff123","confirmPassword":"staff123","fullName":"Staff One"}'

                <span style="color:#6a9955;"># Register as a customer</span>
                curl -X POST http://localhost:8080/api/auth/register \\
                  -H "Content-Type: application/json" \\
                  -d '{"email":"newuser@test.com","password":"mypassword","confirmPassword":"mypassword"}'
                            </pre>

                            <br>
                            <p style="text-align:center;color:#888;"><strong>Happy Coding! 🚀</strong> - This API was built as a practice project for learning frontend-backend integration.</p>
                            </div>
                            """)
                        .contact(new Contact()
                                .name("Practice Project Team")
                                .url("https://github.com/practice-login")
                                .email("support@practicelogin.com"))
                        .license(new License()
                                .name("MIT License - Free to use for learning")
                                .url("https://opensource.org/licenses/MIT")))
                .tags(List.of(
                        new Tag().name("Authentication")
                                .description("""
                                    <b>🔑 PUBLIC ENDPOINTS - No token required</b><br>
                                    These endpoints are used for logging in and registering.<br><br>
                                    <b>Flow:</b><br>
                                    1. User fills login/register form<br>
                                    2. Frontend calls these endpoints<br>
                                    3. For login → save the JWT token from response<br>
                                    4. For register → redirect to login page
                                    """),
                        new Tag().name("Admin")
                                .description("""
                                    <b>🛡️ ADMIN ONLY - Requires ADMIN role</b><br>
                                    Used by admin users to manage the system.<br><br>
                                    <b>Available to:</b> Users with role = ADMIN<br>
                                    <b>Features:</b> Create staff, view/manage all users, change roles, enable/disable/delete users<br><br>
                                    <b>Note:</b> Send JWT token in Authorization header: <code>Bearer &lt;token&gt;</code>
                                    """),
                        new Tag().name("Staff")
                                .description("""
                                    <b>👤 STAFF ACCESS - Requires STAFF or ADMIN role</b><br>
                                    For staff members to view their profile.<br><br>
                                    <b>Available to:</b> Users with role = STAFF or ADMIN<br>
                                    <b>Note:</b> Send JWT token in Authorization header: <code>Bearer &lt;token&gt;</code>
                                    """),
                        new Tag().name("Customer")
                                .description("""
                                    <b>👥 CUSTOMER ACCESS - Requires any authenticated role</b><br>
                                    For customers to view their profile.<br><br>
                                    <b>Available to:</b> All authenticated users (CUSTOMER, STAFF, ADMIN)<br>
                                    <b>Note:</b> Send JWT token in Authorization header: <code>Bearer &lt;token&gt;</code>
                                    """)
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                    <b>How to use JWT Authentication:</b><br><br>
                                    1️⃣ <b>Login</b> via <code>POST /api/auth/login</code><br>
                                    2️⃣ <b>Copy</b> the token from the response<br>
                                    3️⃣ <b>Click "Authorize"</b> button above<br>
                                    4️⃣ <b>Paste</b> your token in the format:<br>
                                    &nbsp;&nbsp;&nbsp;<code>Bearer &lt;your-token&gt;</code><br>
                                    5️⃣ <b>Click "Authorize"</b> to save<br><br>
                                    Now all your requests will include the token automatically! 🎉
                                    """)));
    }
}
