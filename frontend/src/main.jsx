import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App.jsx"
import "./index.css"
import { ChakraProvider } from "@chakra-ui/react"
import { createStandaloneToast } from "@chakra-ui/react"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import Login from "./components/login/Login.jsx"
import AuthProvider from "./context/AuthContext.jsx"

const router = createBrowserRouter([
    {
        path: "/",
        element: <Login />,
    },
    {
        path: "dashboard",
        element: <App />,
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
