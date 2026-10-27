import { ChallengeOverviewCard } from "@/components/challenge-overview-card";
import { GoalCompletionTable } from "@/components/goal-completion-table";
import { ParticipantsCard } from "@/components/participants-card";
import { Props } from "../types";

export function NonPendingChallenge({
  challengeDetails,
  curParticipantId,
  selectedParticipant,
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
      />
      <GoalCompletionTable
        challengeId={challengeDetails.challenge.id}
        canModifyGoalCompletions={
          challengeDetails.challenge.status === "IN_PROGRESS"
        }
        curParticipantId={curParticipantId}
      />
    </>
  );
}
