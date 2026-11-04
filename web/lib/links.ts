type Link = {
  href: string;
  name: string;
};

export const links = {
  dashboard: { href: "/dashboard", name: "Dashboard" },
  createChallenge: { href: "/challenges/create", name: "Create" },
  invites: { href: "/invites", name: "Invites" },
  editAccount: { href: "/account-setup", name: "Edit" },
  logout: { href: "/challenge-tracker/auth/logout", name: "Logout" },
  login: { href: "/challenge-tracker/auth/login", name: "Login" },
} satisfies Record<string, Link>;
