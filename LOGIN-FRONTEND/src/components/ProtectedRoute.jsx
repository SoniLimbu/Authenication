import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import Navbar from "./Navbar";

const ProtectedRoute = ({ allowedRoles }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // Check if user is logged in
  if (!token || !role) {
    return <Navigate to="/" replace />;
  }

  // Check if user's role is in the allowedRoles array
  if (allowedRoles && !allowedRoles.includes(role)) {
    return <Navigate to="/access-denied" replace />;
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>
    </div>
  );
};

export default ProtectedRoute;
