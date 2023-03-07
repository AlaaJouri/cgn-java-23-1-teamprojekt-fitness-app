



import axios from "axios";
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";

type User = {
    id: string;
    username: string;
    role: string;
}

export default function useAuth (redirectToSignIn?: boolean) {
    const [user, setUser] = useState<User | null>(null);
    const navigate = useNavigate();
    const {pathname} = useLocation();

    useEffect(() => {
        axios.get("/api/user/me").then(res => {
            setUser(res.data);
        }).catch(e => {
            if (redirectToSignIn && e.response.status === 401) {
                window.sessionStorage.setItem("signInRedirect", pathname || "/");
                navigate("/sign-in");
            }
        });
    }, [pathname, navigate, redirectToSignIn]);

    return user;
}