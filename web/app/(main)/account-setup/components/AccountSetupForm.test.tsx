import { accountSetup } from "@/actions/account-setup";
import { components } from "@/types/api";
import { TSetupAccountFormSchema } from "@/types/form-types";
import "@testing-library/jest-dom";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent, { UserEvent } from "@testing-library/user-event";
import { redirect } from "next/navigation";
import { AccountSetupForm } from "./AccountSetupForm";

jest.mock("@/actions/account-setup", () => ({
  accountSetup: jest.fn(),
}));

jest.mock("next/navigation", () => ({
  redirect: jest.fn(),
}));

const mockAccountSetup = jest.mocked(accountSetup);
const mockRedirect = jest.mocked(redirect);

describe("AccountSetupForm", () => {
  const availableAvatars: components["schemas"]["AvatarResponse"][] = [
    { src: "/avatars/panda.png" },
    { src: "/avatars/robot.png" },
    { src: "/avatars/guy.png" },
  ];

  let user: UserEvent;

  beforeEach(() => {
    user = userEvent.setup();
    mockAccountSetup.mockResolvedValue({ success: true, error: undefined });
  });

  it("sends correct first name, last name, username to server (no default avatar)", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "",
      lastName: "",
      username: "",
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.type(screen.getByLabelText("First Name"), "John");
    await user.type(screen.getByLabelText("Last Name"), "Doe");
    await user.type(screen.getByLabelText("Username"), "john_doe_12");

    await user.click(screen.getByRole("button", { name: "Submit" }));

    await waitFor(() => {
      expect(mockAccountSetup).toHaveBeenCalledTimes(1);
    });
    expect(mockAccountSetup).toHaveBeenCalledWith({
      firstName: "John",
      lastName: "Doe",
      username: "john_doe_12",
      avatarSrc: undefined,
    });
  });

  it("sends default values if user doesn't change any fields (with default avatar)", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(0)?.src,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByRole("button", { name: "Submit" }));

    await waitFor(() => {
      expect(mockAccountSetup).toHaveBeenCalledTimes(1);
    });

    expect(mockAccountSetup).toHaveBeenCalledWith({
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(0)?.src,
    });
  });

  it("sends default values if user doesn't change any fields (no default avatar)", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: undefined,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByRole("button", { name: "Submit" }));

    await waitFor(() => {
      expect(mockAccountSetup).toHaveBeenCalledTimes(1);
    });

    expect(mockAccountSetup).toHaveBeenCalledWith({
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: undefined,
    });
  });

  it("sends selected avatar to server", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: undefined,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByTestId(availableAvatars.at(1)?.src ?? ""));
    await user.click(screen.getByRole("button", { name: "Submit" }));

    await waitFor(() => {
      expect(mockAccountSetup).toHaveBeenCalledTimes(1);
    });

    expect(mockAccountSetup).toHaveBeenCalledWith({
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(1)?.src,
    });
  });

  it("puts border only on default avatar", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(2)?.src,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    expect(screen.getByTestId(availableAvatars.at(0)?.src ?? "")).toHaveClass(
      "border-transparent"
    );
    expect(screen.getByTestId(availableAvatars.at(1)?.src ?? "")).toHaveClass(
      "border-transparent"
    );

    expect(screen.getByTestId(availableAvatars.at(2)?.src ?? "")).toHaveClass(
      "border-black"
    );
  });

  it("puts border on selected avatar (starting without default avatar)", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: undefined,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByTestId(availableAvatars.at(1)?.src ?? ""));

    expect(screen.getByTestId(availableAvatars.at(0)?.src ?? "")).toHaveClass(
      "border-transparent"
    );
    expect(screen.getByTestId(availableAvatars.at(1)?.src ?? "")).toHaveClass(
      "border-black"
    );

    expect(screen.getByTestId(availableAvatars.at(2)?.src ?? "")).toHaveClass(
      "border-transparent"
    );
  });

  it("puts border on selected avatar (starting with default avatar)", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(2)?.src,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByTestId(availableAvatars.at(0)?.src ?? ""));

    expect(screen.getByTestId(availableAvatars.at(0)?.src ?? "")).toHaveClass(
      "border-black"
    );
    expect(screen.getByTestId(availableAvatars.at(1)?.src ?? "")).toHaveClass(
      "border-transparent"
    );

    expect(screen.getByTestId(availableAvatars.at(2)?.src ?? "")).toHaveClass(
      "border-transparent"
    );
  });

  it("redirects to dashboard on success", async () => {
    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(0)?.src,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByRole("button", { name: "Submit" }));

    expect(mockRedirect).toHaveBeenCalledWith("/dashboard");
  });

  it("does not redirect if error", async () => {
    const errorMsg = "database error";
    mockAccountSetup.mockResolvedValue({
      success: false,
      error: { detail: errorMsg },
    });

    const defaultValues: TSetupAccountFormSchema = {
      firstName: "Bob",
      lastName: "Smith",
      username: "bbsmith99",
      avatarSrc: availableAvatars.at(0)?.src,
    };

    render(
      <AccountSetupForm
        availableAvatars={availableAvatars}
        defaultValues={defaultValues}
      />
    );

    await user.click(screen.getByRole("button", { name: "Submit" }));

    expect(mockRedirect).not.toHaveBeenCalled();
    expect(
      await screen.findByText(errorMsg, { exact: false })
    ).toBeInTheDocument();
  });
});
