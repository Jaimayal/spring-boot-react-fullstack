import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App.jsx"
import "./index.css"
import { ChakraProvider } from "@chakra-ui/react"
import { createStandaloneToast } from "@chakra-ui/react"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import Login from "./components/login/Login.jsx"
import AuthProvider from "./context/AuthContext.jsx"
import ProtectedRouteProvider from "./components/shared/ProtectedRouteProvider.jsx"

const router = createBrowserRouter([
    // {
    //     path: "/*",
    //     element: <ProtectedRouteProvider />,
    // },
    {
        path: "/",
        element: <Login />,
    },
    {
        path: "dashboard",
        element: (
            <ProtectedRouteProvider>
                <App />
            </ProtectedRouteProvider>
        ),
    },
])

const { ToastContainer } = createStandaloneToast()
ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <ChakraProvider>
            <AuthProvider>
                <RouterProvider router={router} />
            </AuthProvider>
            <ToastContainer />
        </ChakraProvider>
    </React.StrictMode>
)
