import { auth0 } from "./auth0";
import { getValidFirstTimeUser, getValidUser } from "./auth-utils";
import { redirect } from "next/navigation";
import { client } from "./api-client";

jest.mock("./auth0", () => ({
  auth0: {
    getSession: jest.fn(),
  },
}));

jest.mock("next/navigation", () => ({
  redirect: jest.fn(() => {
    throw new Error("NEXT_REDIRECT");
  }),
}));

jest.mock("./api-client");

describe("getValidUser", () => {
  it("redirects to login if user is not authenticated", async () => {
    const mockedAuth0 = jest.mocked(auth0);
    mockedAuth0.getSession.mockResolvedValue(null);

    await expect(getValidUser()).rejects.toThrow("NEXT_REDIRECT");

    expect(redirect).toHaveBeenCalledWith("/auth/login");
  });

  it("redirects to account setup if user's account isn't set up", async () => {
    const mockedAuth0 = jest.mocked(auth0);
    mockedAuth0.getSession.mockResolvedValue({ user: "john" } as any);

    const mockedClient = jest.mocked(client);
    mockedClient.GET.mockReturnValue({ data: { value: false } } as any);

    await expect(getValidUser()).rejects.toThrow("NEXT_REDIRECT");

    expect(redirect).toHaveBeenCalledWith("/account-setup");
  });

  it("returns valid session if session is valid and account is set up", async () => {
    const userData = { user: "john" };
    const mockedAuth0 = jest.mocked(auth0);
    mockedAuth0.getSession.mockResolvedValue(userData as any);

    const mockedClient = jest.mocked(client);
    mockedClient.GET.mockReturnValue({ data: { value: true } } as any);

    const res = await getValidUser();
    expect(res).toEqual({ user: "john" });
  });
});

describe("getValidFirstTimeuser", () => {
  it("redirects to login if user is not authenticated", async () => {
    const mockedAuth0 = jest.mocked(auth0);
    mockedAuth0.getSession.mockResolvedValue(null);

    await expect(getValidFirstTimeUser()).rejects.toThrow("NEXT_REDIRECT");

    expect(redirect).toHaveBeenCalledWith("/auth/login");
  });

  it("returns valid session if session is valid and account is set up", async () => {
    const userData = { user: "john" };
    const mockedAuth0 = jest.mocked(auth0);
    mockedAuth0.getSession.mockResolvedValue(userData as any);

    const mockedClient = jest.mocked(client);
    mockedClient.GET.mockReturnValue({ data: { value: true } } as any);

    const res = await getValidFirstTimeUser();
    expect(res).toEqual({ user: "john" });
  });
});
