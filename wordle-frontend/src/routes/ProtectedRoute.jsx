import { useState, useEffect } from "react";
import { Navigate, Outlet } from "react-router-dom";

import { useAuth } from "../provider/authProvider";

const ProtectedRoute = () => {
  const { isAuthenticated, isInitialized } = useAuth();
  const [checked, setChecked] = useState(null);

  useEffect(() => {
    if (isInitialized) {
      setChecked(isAuthenticated);
    }
  }, [isInitialized, isAuthenticated]);

  if (checked == null) {
    return null;
  }

  // If authenticated, render the child route
  if (checked === true) {
    return (
      <div className="w-full min-h-screen flex flex-row bg-sidebar">
        <div className='flex flex-col gap-[44px] bg-main w-full rounded-3xl px-[60px] py-6'>
          <Outlet />
        </div>
      </div>
    );
  }

  // Check if the user is authenticated
  else {
    // If not authenticated, redirect to the login page
    return <Navigate to="/" />;
  }
};

export default ProtectedRoute;
