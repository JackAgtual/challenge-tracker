import { render, screen, waitFor } from "@testing-library/react";
import userEvent, { UserEvent } from "@testing-library/user-event";

import { removeParticipant } from "@/actions/participant-remove";
import { toastProblemDetail } from "@/lib/toast-utils";
import { RemoveParticipantButton } from "./RemoveParticipantButton";

const push = jest.fn();
const path = "/challenges/1";

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push }),
  usePathname: () => path,
}));

jest.mock("@/actions/participant-remove", () => ({
  removeParticipant: jest.fn(),
}));

jest.mock("@/lib/toast-utils", () => ({
  toastProblemDetail: jest.fn(),
}));

const mockedRemoveParticipant = jest.mocked(removeParticipant);

describe("RemoveParticipantButton", () => {
  const removeSelectedParticipant = "Remove selected participant";

  let user: UserEvent;

  beforeEach(() => {
    user = userEvent.setup();
    jest.clearAllMocks();
  });

  async function openAndConfirm() {
    await user.click(
      screen.getByRole("button", { name: removeSelectedParticipant })
    );
    await user.click(await screen.findByRole("button", { name: "Remove" }));
  }

  it("calls the action and navigates to the bare pathname on success", async () => {
    mockedRemoveParticipant.mockResolvedValue({ error: undefined } as any);

    render(
      <RemoveParticipantButton
        challengeId={1}
        participantId={42}
        disabled={false}
      />
    );

    await openAndConfirm();

    await waitFor(() => {
      expect(mockedRemoveParticipant).toHaveBeenCalledWith(1, 42);
      expect(push).toHaveBeenCalledWith(path);
    });
  });

  it("shows a toast and does not navigate on error", async () => {
    const error = { title: "Nope", status: 400 };
    mockedRemoveParticipant.mockResolvedValue({ error } as any);

    render(
      <RemoveParticipantButton
        challengeId={1}
        participantId={42}
        disabled={false}
      />
    );
    await openAndConfirm();

    await waitFor(() => {
      expect(toastProblemDetail).toHaveBeenCalledWith(error);
    });
    expect(push).not.toHaveBeenCalled();
  });

  it("is disabled when the disabled prop is true", () => {
    render(
      <RemoveParticipantButton challengeId={1} participantId={42} disabled />
    );
    expect(
      screen.getByRole("button", { name: removeSelectedParticipant })
    ).toBeDisabled();
  });

  it("closes the dialog after a successful removal", async () => {
    mockedRemoveParticipant.mockResolvedValue({ error: undefined } as any);

    render(
      <RemoveParticipantButton
        challengeId={1}
        participantId={42}
        disabled={false}
      />
    );

    await user.click(
      screen.getByRole("button", { name: removeSelectedParticipant })
    );
    expect(await screen.findByRole("alertdialog")).toBeInTheDocument();

    await user.click(screen.getByRole("button", { name: "Remove" }));

    await waitFor(() => {
      expect(screen.queryByRole("alertdialog")).not.toBeInTheDocument();
    });
  });

  it("closes the dialog when the removal fails", async () => {
    mockedRemoveParticipant.mockResolvedValue({
      error: { detail: "Nope" },
    } as any);

    render(
      <RemoveParticipantButton
        challengeId={1}
        participantId={42}
        disabled={false}
      />
    );

    await user.click(
      screen.getByRole("button", { name: removeSelectedParticipant })
    );
    expect(await screen.findByRole("alertdialog")).toBeInTheDocument();

    await user.click(screen.getByRole("button", { name: "Remove" }));

    await waitFor(() => {
      expect(screen.queryByRole("alertdialog")).not.toBeInTheDocument();
    });
  });
});
