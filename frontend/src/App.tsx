import { useEffect, useState } from "react";
import {
  Link,
  Route,
  Routes,
  useLocation,
  useNavigate,
  useParams,
} from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;

function Home() {
  return (
    <div className="text-gray-800 text-2xl">
      Home
      <ul className="text-xl text-indigo-600 underline mt-4">
        <li>
          <Link to="/users" className="hover:text-indigo-800">
            Users ListHe
          </Link>
        </li>
        <li>
          <Link to="/createUser" className="hover:text-indigo-800">
            Create User
          </Link>
        </li>
      </ul>
    </div>
  );
}

function UserCreateForm() {
  const [name, setName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);
    if (!name.trim() || !email.trim()) {
      setError("Name and email are required");
      return;
    }
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/api/users`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: name.trim(),
          email: email.trim(),
          phoneNumber: phoneNumber.trim(),
        }),
      });
      if (!res.ok) {
        const text = await res.json();
        throw new Error(JSON.stringify(text));
      }
      setSuccess(true);
      setName("");
      setPhoneNumber("");
      setEmail("");
    } catch (err: any) {
      setError(err.message || "Failed to create user");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Create User</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Name
          </label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
            placeholder="Full name"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Email
          </label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
            placeholder="email@example.com"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Phone Number
          </label>
          <input
            type="number"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
            placeholder="0x xx xx xx xx"
          />
        </div>
        <div>
          <button
            type="submit"
            disabled={loading}
            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 disabled:bg-gray-400 transition"
          >
            {loading ? "Creating..." : "Create User"}
          </button>
          {success && (
            <span className="ml-3 text-emerald-600 font-medium">Created</span>
          )}
          {error && (
            <span className="ml-3 text-rose-600 font-medium">{error}</span>
          )}
        </div>
      </form>
    </div>
  );
}

function UserUpdateForm() {
  const { state } = useLocation();
  const passedUser = state?.user;

  const params = new URLSearchParams(window.location.search);
  const id = passedUser?.id ?? params.get("id");

  const [name, setName] = useState(passedUser?.name ?? "");
  const [phoneNumber, setPhoneNumber] = useState(passedUser?.phoneNumber ?? "");
  const [email, setEmail] = useState(passedUser?.email ?? "");
  const [loading, setLoading] = useState(false);
  const [initialLoading, setInitialLoading] = useState(!passedUser);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (passedUser) {
      setInitialLoading(false);
      return;
    }

    if (!id) {
      setError("Missing user id");
      setInitialLoading(false);
      return;
    }

    const fetchUser = async () => {
      try {
        const res = await fetch(`${API_URL}/api/users/${id}`);
        if (!res.ok) {
          throw new Error("Failed to fetch user");
        }
        const fetchedUser = await res.json();
        setName(fetchedUser.name ?? "");
        setEmail(fetchedUser.email ?? "");
        setPhoneNumber(fetchedUser.phoneNumber ?? "");
      } catch (err: any) {
        setError(err.message || "Failed to load user");
      } finally {
        setInitialLoading(false);
      }
    };

    fetchUser();
  }, [id, passedUser]);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);

    if (!id) {
      setError("Missing user id");
      return;
    }

    if (!name.trim() || !email.trim()) {
      setError("Name and email are required");
      return;
    }

    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/api/users/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: name.trim(),
          email: email.trim(),
          phoneNumber: phoneNumber.trim(),
        }),
      });

      if (!res.ok) {
        throw new Error("Update failed");
      }

      setSuccess(true);
      setTimeout(() => {
        window.location.href = "/users";
      }, 700);
    } catch (err: any) {
      setError(err.message || "Failed to update user");
    } finally {
      setLoading(false);
    }
  };

  if (initialLoading) {
    return <div className="p-4 text-gray-600">Loading user...</div>;
  }

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Update User</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Name
          </label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-amber-500"
            placeholder="Full name"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Email
          </label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-amber-500"
            placeholder="email@example.com"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Phone Number
          </label>
          <input
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-amber-500"
            placeholder="0x xx xx xx xx"
          />
        </div>
        <div className="flex items-center gap-3">
          <button
            type="submit"
            disabled={loading}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 disabled:bg-gray-400 transition"
          >
            {loading ? "Updating..." : "Update User"}
          </button>
          <Link
            to="/users"
            className="text-indigo-600 hover:text-indigo-800 underline"
          >
            Cancel
          </Link>
          {success && (
            <span className="ml-3 text-emerald-600 font-medium">Updated</span>
          )}
          {error && (
            <span className="ml-3 text-rose-600 font-medium">{error}</span>
          )}
        </div>
      </form>
    </div>
  );
}

function UsersList({ users }: { users: any[] }) {
  return (
    <div>
      <h1 className="text-bold text-2xl text-center text-white bg-indigo-600 py-3">
        Users List
      </h1>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-gray-200 text-left border-b">
              <th className="p-3 font-semibold text-gray-700">ID</th>
              <th className="p-3 font-semibold text-gray-700">Name</th>
              <th className="p-3 font-semibold text-gray-700">Email</th>
              <th className="p-3 font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 ? (
              <tr>
                <td colSpan={4} className="p-3 text-center text-gray-500">
                  No users
                </td>
              </tr>
            ) : (
              users.map((user: any) => (
                <tr key={user.id} className="border-b hover:bg-gray-50">
                  <td className="p-3 text-gray-800">{user.id}</td>
                  <td className="p-3 text-gray-800">{user.name}</td>
                  <td className="p-3 text-gray-800">{user.email}</td>
                  <td className="p-3 space-x-3">
                    <Link
                      to={`/users/${user.id}`}
                      className="text-indigo-600 hover:text-indigo-800 underline"
                      state={{ user }}
                    >
                      Show
                    </Link>
                    <Link
                      to={`/UpdateUser?id=${user.id}`}
                      className="text-amber-600 hover:text-amber-800 underline"
                      state={{ user }}
                    >
                      Edit
                    </Link>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function UserDetails() {
  const { id } = useParams();
  const [user, setUser] = useState<any | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setError("Missing user id");
      setLoading(false);
      return;
    }

    const fetchUser = async () => {
      try {
        const res = await fetch(`${API_URL}/api/users/${id}`);
        if (!res.ok) {
          throw new Error("Failed to fetch user");
        }
        const data = await res.json();
        setUser(data.user);
      } catch (err: any) {
        setError(err.message || "Failed to load user");
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [id]);

  if (loading) {
    return <div className="p-4 text-gray-600">Loading user...</div>;
  }

  if (error) {
    return <div className="p-4 text-rose-600 font-medium">{error}</div>;
  }

  if (!user || !id) {
    return <div className="p-4 text-gray-600">User not found</div>;
  }

  return (
    <div className="p-6 max-w-md mx-auto">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">User Details</h2>
      <div className="space-y-3 bg-gray-50 p-4 rounded">
        <div>
          <strong className="text-gray-700">ID:</strong>{" "}
          <span className="text-gray-600">{id}</span>
        </div>
        <div>
          <strong className="text-gray-700">Name:</strong>{" "}
          <span className="text-gray-600">{user.name}</span>
        </div>
        <div>
          <strong className="text-gray-700">Email:</strong>{" "}
          <span className="text-gray-600">{user.email}</span>
        </div>
        <div>
          <strong className="text-gray-700">Phone:</strong>{" "}
          <span className="text-gray-600">{user.phoneNumber ?? "-"}</span>
        </div>
      </div>
      <div className="mt-6 space-x-3">
        <Link
          to="/users"
          className="text-indigo-600 hover:text-indigo-800 underline"
        >
          Back to list
        </Link>
        <Link
          to={`/UpdateUser?id=${user.id}`}
          state={{ user }}
          className="text-amber-600 hover:text-amber-800 underline"
        >
          Edit
        </Link>
      </div>
    </div>
  );
}
function HealthIndicator() {
  const [isApiHealthy, setIsApiHealthy] = useState(true);
  const [isMessageHealthy, setIsMessageHealthy] = useState(true);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkApiHealth = async () => {
      try {
        const res = await fetch(`${API_URL}/api/health-api`);

        if (res.ok) setIsApiHealthy(true);
      } catch {
        setIsApiHealthy(false);
      }
    };

    const checkMessageServerHealth = async () => {
      try {
        const res = await fetch(`${API_URL}/api/health-message-server`);
        if (res.ok) setIsMessageHealthy(true);
      } catch {
        setIsMessageHealthy(false);
      }
    };

    const runChecks = async () => {
      setLoading(true); // start loading
      await Promise.all([checkApiHealth(), checkMessageServerHealth()]);
      setLoading(false); // end loading ONLY once
    };

    runChecks();
    const interval = setInterval(runChecks, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="flex gap-1 flex-col">
      <div className="flex items-center gap-2">
        <div
          className={`w-4 h-4 rounded-full transition ${
            isApiHealthy ? "bg-emerald-500" : "bg-rose-500"
          } ${loading ? "opacity-50" : "opacity-100"}`}
        />
        <span className="text-sm text-white font-medium">
          {isApiHealthy ? "Api Server Up" : "Api Server Down"}
        </span>
      </div>

      <div className="flex items-center gap-2 ml-auto">
        <div
          className={`w-4 h-4 rounded-full transition ${
            isMessageHealthy ? "bg-emerald-500" : "bg-rose-500"
          } ${loading ? "opacity-50" : "opacity-100"}`}
        />
        <span className="text-sm text-white font-medium">
          {isMessageHealthy ? "Message Server Up" : "Message Server Down"}
        </span>
      </div>
    </div>
  );
}
function App() {
  const [users, setusers] = useState([]);
  const location = useLocation();

  useEffect(() => {
    const fetchUsers = async () => {
      const res = await fetch(`${API_URL}/api/users`);
      const users = await res.json();
      console.log("users log : ", users);
      setusers(users.users);
    };
    fetchUsers();
  }, [location.pathname]);

  return (
    <>
      <div>
        <nav className="bg-gradient-to-r from-indigo-600 to-indigo-700 shadow-lg">
          <div className="flex gap-8 items-center justify-between px-6 py-3">
            <ul className="flex gap-6">
              <li className="hover:bg-indigo-500 px-3 py-2 rounded transition">
                <Link to="/users" className="text-white font-medium">
                  Users List
                </Link>
              </li>
              <li className="hover:bg-indigo-500 px-3 py-2 rounded transition">
                <Link to="/createUser" className="text-white font-medium">
                  Create User
                </Link>
              </li>
              <li className="hover:bg-indigo-500 px-3 py-2 rounded transition">
                <Link to="/send-otp" className="text-white font-medium">
                  Send OTP
                </Link>
              </li>
            </ul>
            <HealthIndicator />
          </div>
        </nav>
        <div className="w-full max-w-5xl mx-auto my-6 border border-gray-200 rounded-lg shadow-sm">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/users" element={<UsersList users={users} />} />
            <Route path="/createUser" element={<UserCreateForm />} />
            <Route path="/UpdateUser" element={<UserUpdateForm />} />
            <Route path="/users/:id" element={<UserDetails />} />
            <Route path="/send-otp" element={<Login />} />
            <Route path="/verify-otp" element={<VerifyOtp />} />
          </Routes>
        </div>
      </div>
    </>
  );
}

export default App;

function VerifyOtp() {
  const location = useLocation();
  const email = location.state?.email ?? "";
  const [otp, setOtp] = useState("");
  const [message, setMessage] = useState("");

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const res = await fetch(`${API_URL}/api/auth/verify-otp`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, otp }),
      });

      const text = await res.text();
      setMessage(text);
    } catch (err) {
      setMessage("Error verifying OTP");
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto bg-white rounded-lg shadow">
      <h2 className="text-2xl font-bold mb-4">
        Verify OTP for <strong>{email}</strong>
      </h2>

      <form onSubmit={handleVerifyOtp} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">OTP Code</label>
          <input
            type="text"
            className="w-full p-2 border rounded"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            placeholder="Enter OTP"
          />
        </div>

        <button
          type="submit"
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
        >
          Verify OTP
        </button>
      </form>

      {message && (
        <div className="mt-4 p-2 bg-gray-100 border rounded text-sm">
          {message}
        </div>
      )}
    </div>
  );
}
function Login() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSendOtp = async (e) => {
    e.preventDefault();
    setMessage("");
    setLoading(true); // <-- show "Sending..."

    try {
      const res = await fetch(`${API_URL}/api/auth/send-otp?email=${email}`, {
        method: "POST",
      });

      const text = await res.text();
      if (res.ok) {
        setMessage(text);
        navigate("/verify-otp", { state: { email: email } });
      }
      if (res.status == 500) {
        setError(text);
      }

      // Only navigate if OTP sent successfully:
    } catch (err) {
      setMessage("Error sending OTP");
    } finally {
      setLoading(false); // <-- remove loading state
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto bg-white rounded-lg shadow">
      <h2 className="text-2xl font-bold mb-4">Send OTP</h2>

      <form onSubmit={handleSendOtp} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">Email :</label>
          <input
            type="text"
            className="w-full p-2 border rounded"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter email"
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className={`px-4 py-2 rounded text-white transition ${
            loading
              ? "bg-indigo-400 cursor-not-allowed"
              : "bg-indigo-600 hover:bg-indigo-700"
          }`}
        >
          {loading ? "Sending OTP..." : "Send OTP"}
        </button>
      </form>

      {error && !loading && !message && (
        <div className="mt-4 p-2 bg-red-100 text-red-500 border rounded text-sm">
          {error}
        </div>
      )}
    </div>
  );
}
