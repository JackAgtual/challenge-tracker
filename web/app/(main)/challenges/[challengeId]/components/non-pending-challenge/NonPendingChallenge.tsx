import { ChallengeOverviewCard } from "@/components/challenge-overview-card";
import { GoalCompletionTable } from "@/components/goal-completion-table";
import { ParticipantsCard } from "@/components/participants-card";
import { Props } from "../types";

export function NonPendingChallenge({
  challengeDetails,
  curParticipantId,
  selectedParticipantId,
}: Props) {
  return (
    <>
      <ChallengeOverviewCard
        challenge={challengeDetails.challenge}
        canEditAndStartChallenge={false}
      />
      <ParticipantsCard
        challengeId={challengeDetails.challenge.id}
        participants={challengeDetails.participants}
        challengeIsPending={false}
        selectedParticipantId={selectedParticipantId}
      />
      <GoalCompletionTable
        challengeId={challengeDetails.challenge.id}
        canModifyGoalCompletions={
          challengeDetails.challenge.status === "IN_PROGRESS" &&
          curParticipantId === selectedParticipantId
        }
        participantId={selectedParticipantId}
      />
    </>
  );
}
