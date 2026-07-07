import React, { useEffect, useState } from "react";
import { getCustomerProfile } from "../api/AuthService";

const CustomerDashboard = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getCustomerProfile();
        setProfile(response.data.data);
      } catch (error) {
        console.error("Error fetching customer profile", error);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, []);

  if (loading) return <div className="p-8">Loading customer profile...</div>;

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Customer Dashboard</h1>
      {profile && (
        <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 text-2xl font-bold">
              {profile.fullName ? profile.fullName.charAt(0) : "C"}
            </div>
            <div>
              <h2 className="text-xl font-bold text-gray-900">{profile.fullName || profile.username}</h2>
              <p className="text-gray-500">{profile.email}</p>
            </div>
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 pt-6 border-t border-gray-100">
            <div>
              <p className="text-sm font-medium text-gray-500">Role</p>
              <p className="mt-1 font-semibold text-gray-900">{profile.role}</p>
            </div>
            <div>
              <p className="text-sm font-medium text-gray-500">Member Since</p>
              <p className="mt-1 font-semibold text-gray-900">Recently Joined</p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CustomerDashboard;
