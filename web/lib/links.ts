type Link = {
  href: string;
  name: string;
};

export const basePath = "/challenge-tracker";

export const links = {
  dashboard: { href: "/dashboard", name: "Dashboard" },
  createChallenge: { href: "/challenges/create", name: "Create" },
  invites: { href: "/invites", name: "Invites" },
  editAccount: { href: "/account-setup", name: "Edit" },
  logout: { href: `${basePath}/auth/logout`, name: "Logout" },
  login: { href: `${basePath}/auth/login`, name: "Login" },
} satisfies Record<string, Link>;

export function applicationUrl(path = "") {
  const appBaseUrl = process.env.APP_BASE_URL;

  if (!appBaseUrl) {
    throw new Error("APP_BASE_URl must be configured");
  }

  const baseUrl = new URL(appBaseUrl);

  // make sure base url ends with 1 trailing slash
  baseUrl.pathname = baseUrl.pathname.replace(/\/+$/, "") + "/";

  // remove starting slash(es) from path, append to baseUrl
  return new URL(path.replace(/^\/+/, ""), baseUrl);
}
