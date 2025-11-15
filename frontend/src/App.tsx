import { useEffect, useState } from "react";
import { Link, Route, Routes, useLocation } from "react-router-dom";

function Home() {
  return (
    <div className="text-black text-2xl">
      Home
      <ul className="text-xl text-blue-600 underline">
        <li>
          <Link to="/users">Users List</Link>
        </li>
        <li>
          <Link to="/createUser">Create User</Link>
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
      const res = await fetch("http://localhost:8080/api/users", {
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
      //TODO: decide
      // optional: navigate to /users instead of reloading
      // window.location.href = "/users";
    } catch (err: any) {
      setError(err.message || "Failed to create user");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h2 className="text-2xl mb-4">Create User</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm mb-1">Name</label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border p-2"
            placeholder="Full name"
          />
        </div>
        <div>
          <label className="block text-sm mb-1">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border p-2"
            placeholder="email@example.com"
          />
        </div>
        <div>
          <label className="block text-sm mb-1">Phone Number</label>
          <input
            type="number"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="w-full border p-2"
            placeholder="0x xx xx xx xx"
          />
        </div>
        <div>
          <button
            type="submit"
            disabled={loading}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:text-blue-500 hover:bg-white hover:border"
          >
            {loading ? "Creating..." : "Create User"}
          </button>
          {success && <span className="ml-3 text-green-600">Created</span>}
          {error && <span className="ml-3 text-red-600">{error}</span>}
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

  // Initialize with passed user data if available
  const [name, setName] = useState(passedUser?.name ?? "");
  const [phoneNumber, setPhoneNumber] = useState(passedUser?.phoneNumber ?? "");
  const [email, setEmail] = useState(passedUser?.email ?? "");
  const [loading, setLoading] = useState(false);
  const [initialLoading, setInitialLoading] = useState(!passedUser);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  // Fetch user if no state was passed (direct URL navigation)
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
        const res = await fetch(`http://localhost:8080/api/users/${id}`);
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
      const res = await fetch(`http://localhost:8080/api/users/${id}`, {
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
    return <div className="p-4">Loading user...</div>;
  }

  return (
    <div className="p-4 max-w-md mx-auto">
      <h2 className="text-2xl mb-4">Update User</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm mb-1">Name</label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border p-2"
            placeholder="Full name"
          />
        </div>
        <div>
          <label className="block text-sm mb-1">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border p-2"
            placeholder="email@example.com"
          />
        </div>
        <div>
          <label className="block text-sm mb-1">Phone Number</label>
          <input
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="w-full border p-2"
            placeholder="0x xx xx xx xx"
          />
        </div>
        <div className="flex items-center gap-3">
          <button
            type="submit"
            disabled={loading}
            className="bg-yellow-600 text-white px-4 py-2 rounded hover:text-yellow-500 hover:bg-white hover:border"
          >
            {loading ? "Updating..." : "Update User"}
          </button>
          <Link to="/users" className="text-blue-600 underline">
            Cancel
          </Link>
          {success && <span className="ml-3 text-green-600">Updated</span>}
          {error && <span className="ml-3 text-red-600">{error}</span>}
        </div>
      </form>
    </div>
  );
}
function UsersList({ users }: { users: any[] }) {
  return (
    <div>
      <h1 className="text-bold text-2xl text-center  text-green-50 bg-green-500">
        users List :
      </h1>
      <table className="min-w-full border-collapse">
        <thead>
          <tr className="text-left border-b">
            <th className="p-2">ID</th>
            <th className="p-2">Name</th>
            <th className="p-2">Email</th>
            <th className="p-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.length === 0 ? (
            <tr>
              <td colSpan={4} className="p-2">
                No users
              </td>
            </tr>
          ) : (
            users.map((user: any) => (
              <tr key={user.id} className="border-b">
                <td className="p-2">{user.id}</td>
                <td className="p-2">{user.name}</td>
                <td className="p-2">{user.email}</td>
                <td className="p-2 space-x-2">
                  <Link
                    to={`/users/${user.id}`}
                    className="text-blue-600 underline"
                  >
                    Show
                  </Link>
                  <Link
                    to={`/UpdateUser?id=${user.id}`}
                    className="text-yellow-600 underline"
                    state={{ user }}
                  >
                    Edit
                  </Link>
                  <button className="text-red-600 underline">Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
function App() {
  const [users, setusers] = useState([]);
  const location = useLocation();

  useEffect(() => {
    const fetchUsers = async () => {
      const res = await fetch("http://localhost:8080/api/users");
      const users = await res.json();
      console.log("users log : ", users);
      setusers(users.users);
    };
    fetchUsers();
  }, [location.pathname]);
  return (
    <>
      <div>
        <ul className="text-xl text-cyan-200 underline bg-amber-800 flex  gap-5 items-center justify-center py-2 ">
          <li className="hover:bg-yellow-200 hover:text-blue-600">
            <Link to="/users">Users List</Link>
          </li>
          <li className="hover:bg-yellow-200 hover:text-blue-600">
            <Link to="/createUser">Create User</Link>
          </li>
        </ul>
        <div className="min-w-3/4 w-3/4 mx-auto border-2 my-2 border-red-500 ">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/users" element={<UsersList users={users} />} />
            <Route path="/createUser" element={<UserCreateForm />} />
            <Route path="/UpdateUser" element={<UserUpdateForm />} />
            <Route
              path="/users/:id"
              element={<div className="p-4">User details not implemented</div>}
            />
          </Routes>
        </div>
      </div>
    </>
  );
}

export default App;
