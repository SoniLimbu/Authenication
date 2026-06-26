import React, { useState } from "react";
import API from "../api/authService";

const AuthPage = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });
  const [registerData, setRegisterData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
  });


  const handleLoginSubmit = (e) => {
    e.preventDefault();
    console.log("Login:", loginData); 
  };

  // Handle registration form submission
  const handleRegisterSubmit = (e) => {
    e.preventDefault(); 
    console.log("Register:", registerData);
  };

  return (
    // Full viewport height, centered content with background color
    <div className="min-h-screen bg-slate-100 flex items-center justify-center p-4">
      {/* Card container – width is now responsive: full on mobile, max 448px on larger screens */}
      <div className="w-full max-w-md p-8 bg-white rounded-2xl">
        {/* Heading changes based on login/register mode */}
        <h2 className="text-3xl font-bold text-center mb-2">
          {isLogin ? "Welcome Back" : "Create Account"}
        </h2>

        {/* Subtitle changes based on mode */}
        <p className="text-gray-500 text-center mb-8">
          {isLogin ? "Login to continue" : "Register as a customer"}
        </p>

        {/* Conditional rendering: show Login form if isLogin true, else Register form */}
        {isLogin ? (
          // LOGIN FORM
          <form onSubmit={handleLoginSubmit} className="space-y-5">
            <div>
              <label className="block mb-2 font-medium">Email</label>
              <input
                type="email"
                placeholder="Enter email"
                className="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={loginData.email}
                onChange={(e) =>
                  setLoginData({ ...loginData, email: e.target.value })
                }
              />
            </div>

            <div>
              <label className="block mb-2 font-medium">Password</label>
              <input
                type="password"
                placeholder="Enter password"
                className="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={loginData.password}
                onChange={(e) =>
                  setLoginData({ ...loginData, password: e.target.value })
                }
              />
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
            >
              Login
            </button>
          </form>
        ) : (
          // REGISTER FORM
          <form onSubmit={handleRegisterSubmit} className="space-y-5">
            <div>
              <label className="block mb-2 font-medium">Email</label>
              <input
                type="email"
                placeholder="Enter email"
                className="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={registerData.email}
                onChange={(e) =>
                  setRegisterData({ ...registerData, email: e.target.value })
                }
              />
            </div>

            <div>
              <label className="block mb-2 font-medium">Password</label>
              <input
                type="password"
                placeholder="Enter password"
                className="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={registerData.password}
                onChange={(e) =>
                  setRegisterData({ ...registerData, password: e.target.value })
                }
              />
            </div>

            <div>
              <label className="block mb-2 font-medium">Confirm Password</label>
              <input
                type="password"
                placeholder="Confirm password"
                className="w-full border rounded-lg px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={registerData.confirmPassword}
                onChange={(e) =>
                  setRegisterData({
                    ...registerData,
                    confirmPassword: e.target.value,
                  })
                }
              />
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition"
            >
              Create Account
            </button>
          </form>
        )}

        {/* Toggle between Login and Register */}
        <div className="mt-6 text-center">
          {isLogin ? (
            <p>
              Don't have an account?{" "}
              <button
                onClick={() => setIsLogin(false)}
                className="text-blue-600 font-semibold"
              >
                Register
              </button>
            </p>
          ) : (
            <p>
              Already have an account?{" "}
              <button
                onClick={() => setIsLogin(true)}
                className="text-blue-600 font-semibold"
              >
                Login
              </button>
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AuthPage;