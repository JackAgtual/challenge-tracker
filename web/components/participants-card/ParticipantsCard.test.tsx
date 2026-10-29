import { components } from "@/types/api";
import { render, screen } from "@testing-library/react";
import { ParticipantsCard } from "./ParticipantsCard";

jest.mock("next/navigation");
jest.mock("@/actions/participant-ready", () => ({
  setReady: jest.fn(),
}));
jest.mock("@/actions/participant-remove", () => ({
  removeParticipant: jest.fn(),
}));

describe("ParticipantsCard", () => {
  const notReadyBtnText = "Not ready";
  const readyBtnText = "Ready";
  const removeParticipantBtnText = "Remove selected participant";

  const participants: components["schemas"]["ParticipantResponse"][] = [
    {
      id: 1,
      isChallengeOwner: true,
      joinedAt: "",
      ready: false,
      user: { id: 1, firstName: "bob", lastName: "smith", username: "bsmith" },
    },
    {
      id: 2,
      isChallengeOwner: false,
      joinedAt: "",
      ready: true,
      user: { id: 2, firstName: "jane", lastName: "doe", username: "jdjd" },
    },
  ];

  describe("pending challenge", () => {
    function renderParticipantsCard({
      participantIdx,
      selectedParticipantIdx,
    }: {
      participantIdx: number;
      selectedParticipantIdx: number;
      pending?: boolean;
    }) {
      const participant = participants.at(participantIdx);
      const selectedParticipant = participants.at(selectedParticipantIdx);
      if (!participant || !selectedParticipant) {
        fail();
      }

      render(
        <ParticipantsCard
          challengeId={1}
          challengeIsPending={true}
          curParticipantId={participant.id}
          curUserIsOwner={participant.isChallengeOwner}
          curUserReadyResponse={{
            eligibleToSetReady: true,
            ready: participant.ready,
          }}
          participants={participants}
          selectedParticipantId={selectedParticipant.id}
        />
      );
    }

    it("displays Ready on toggle ready button if participant is not ready", () => {
      renderParticipantsCard({ participantIdx: 0, selectedParticipantIdx: 0 });
      expect(
        screen.getByRole("button", { name: readyBtnText })
      ).toBeInTheDocument();
    });

    it("displays Ready on toggle ready button if participant is not ready even if self is not selected", () => {
      renderParticipantsCard({
        participantIdx: 0,
        selectedParticipantIdx: 1,
      });
      expect(
        screen.getByRole("button", { name: readyBtnText })
      ).toBeInTheDocument();
    });

    it("displays Not ready on toggle ready button if participant is ready", () => {
      renderParticipantsCard({
        participantIdx: 1,
        selectedParticipantIdx: 1,
      });
      expect(
        screen.getByRole("button", { name: notReadyBtnText })
      ).toBeInTheDocument();
    });

    it("displays Not ready on toggle ready button if participant is ready even if self is not selected", () => {
      renderParticipantsCard({
        participantIdx: 1,
        selectedParticipantIdx: 0,
      });
      expect(
        screen.getByRole("button", { name: notReadyBtnText })
      ).toBeInTheDocument();
    });

    it("renders remove participant button for challenge owner", () => {
      renderParticipantsCard({
        participantIdx: 0,
        selectedParticipantIdx: 0,
      });
      expect(
        screen.getByRole("button", { name: removeParticipantBtnText })
      ).toBeInTheDocument();
    });

    it("does not render remove participant button for challenge owner", () => {
      renderParticipantsCard({
        participantIdx: 1,
        selectedParticipantIdx: 0,
      });
      expect(
        screen.queryByRole("button", { name: removeParticipantBtnText })
      ).not.toBeInTheDocument();
    });

    it("disables remove participant button when challenge owner selected", () => {
      renderParticipantsCard({
        participantIdx: 0,
        selectedParticipantIdx: 0,
      });
      expect(
        screen.queryByRole("button", {
          name: removeParticipantBtnText,
        })
      ).toBeDisabled();
    });

    it("enables remove participant button when non-challenge owner selected", () => {
      renderParticipantsCard({
        participantIdx: 0,
        selectedParticipantIdx: 1,
      });
      expect(
        screen.queryByRole("button", {
          name: removeParticipantBtnText,
        })
      ).not.toBeDisabled();
    });

    it("shows correct participant ready count", () => {
      renderParticipantsCard({
        participantIdx: 0,
        selectedParticipantIdx: 0,
      });
      expect(screen.getByText("1 / 2 participants ready")).toBeInTheDocument();
    });
  });

  describe("non-pending challenge", () => {
    function renderParticipantsCard(selectedParticipantIdx: number) {
      const selectedParticipant = participants.at(selectedParticipantIdx);
      if (!selectedParticipant) {
        fail();
      }

      render(
        <ParticipantsCard
          challengeIsPending={false}
          challengeId={1}
          participants={participants}
          selectedParticipantId={selectedParticipant.id}
        />
      );
    }

    it("does not render toggle ready button", () => {
      renderParticipantsCard(1);
      expect(
        screen.queryByRole("button", { name: readyBtnText })
      ).not.toBeInTheDocument();
      expect(
        screen.queryByRole("button", { name: notReadyBtnText })
      ).not.toBeInTheDocument();
    });

    it("does not render remove participant button for owner", () => {
      renderParticipantsCard(0);
      expect(
        screen.queryByRole("button", { name: removeParticipantBtnText })
      ).not.toBeInTheDocument();
    });

    it("does not render remove participant button for non-owner", () => {
      renderParticipantsCard(1);
      expect(
        screen.queryByRole("button", { name: removeParticipantBtnText })
      ).not.toBeInTheDocument();
    });
  });
});
