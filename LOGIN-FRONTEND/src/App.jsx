import { Routes, Route, Navigate } from "react-router-dom";
import "./index.css";
import AuthPage from "./pages/AuthPage";
import AdminDashboard from "./pages/AdminDashboard";
import StaffDashboard from "./pages/StaffDashboard";
import CustomerDashboard from "./pages/CustomerDashboard";
import AccessDenied from "./pages/AccessDenied";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  // Simple check to see if we're already logged in to redirect away from AuthPage
  const isAuthenticated = !!localStorage.getItem("token");
  const role = localStorage.getItem("role");
  
  const getDefaultRoute = () => {
    if (role === "ADMIN") return "/admin";
    if (role === "STAFF") return "/staff";
    return "/customer";
  };

  return (
    <Routes>
      {/* Auth Route */}
      <Route 
        path="/" 
        element={isAuthenticated ? <Navigate to={getDefaultRoute()} replace /> : <AuthPage />} 
      />
      
      {/* Admin Route */}
      <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
        <Route path="/admin" element={<AdminDashboard />} />
      </Route>
      
      {/* Staff Route */}
      <Route element={<ProtectedRoute allowedRoles={["STAFF", "ADMIN"]} />}>
        <Route path="/staff" element={<StaffDashboard />} />
      </Route>
      
      {/* Customer Route */}
      <Route element={<ProtectedRoute allowedRoles={["CUSTOMER", "STAFF", "ADMIN"]} />}>
        <Route path="/customer" element={<CustomerDashboard />} />
      </Route>

      {/* Access Denied */}
      <Route path="/access-denied" element={<AccessDenied />} />
      
      {/* Catch All */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;