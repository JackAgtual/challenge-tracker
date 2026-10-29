import { render, screen } from "@testing-library/react";
import { Participant } from "./Participant";

// Only creating test branches for pending/non-pending, not ready/not-ready, selected/not-selected
describe("Participant", () => {
  function renderParticipant({
    selected,
    pending,
    ready,
  }: {
    selected: boolean;
    pending: boolean;
    ready: boolean;
  }) {
    render(
      <Participant
        challengeIsPending={pending}
        selected={selected}
        participant={{
          id: 1,
          isChallengeOwner: false,
          joinedAt: "",
          user: {
            id: 1,
            firstName: "Jack",
            lastName: "Smith",
            username: "jsmith",
          },
          ready,
        }}
      />
    );
  }

  describe("pending challenge", () => {
    it("displays border if selected", () => {
      renderParticipant({ selected: true, pending: true, ready: true });
      expect(screen.getByRole("link")).toHaveClass("border-black");
    });

    it("displays transparent border if not selected", () => {
      renderParticipant({ selected: false, pending: true, ready: true });
      expect(screen.getByRole("link")).toHaveClass("border-transparent");
    });

    it("shows ready if participant is ready", () => {
      renderParticipant({ selected: true, pending: true, ready: true });
      expect(screen.getByText("Ready")).toBeInTheDocument();
    });

    it("shows not ready if participant is not ready", () => {
      renderParticipant({ selected: true, pending: true, ready: false });
      expect(screen.getByText("Not ready")).toBeInTheDocument();
    });
  });

  describe("non pending challenge", () => {
    it("displays border if selected", () => {
      renderParticipant({ selected: true, pending: false, ready: true });
      expect(screen.getByRole("link")).toHaveClass("border-black");
    });

    it("displays transparent border if not selected", () => {
      renderParticipant({ selected: false, pending: false, ready: true });
      expect(screen.getByRole("link")).toHaveClass("border-transparent");
    });

    it("does not show ready status even if participant is ready", () => {
      renderParticipant({ selected: true, pending: false, ready: true });
      expect(screen.queryByText("Ready")).not.toBeInTheDocument();
      expect(screen.queryByText("Not ready")).not.toBeInTheDocument();
    });

    it("does not show ready status even if participant is not ready", () => {
      renderParticipant({ selected: true, pending: false, ready: false });
      expect(screen.queryByText("Ready")).not.toBeInTheDocument();
      expect(screen.queryByText("Not ready")).not.toBeInTheDocument();
    });
  });
});
