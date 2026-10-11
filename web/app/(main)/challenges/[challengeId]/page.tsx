import ChallengeOverviewCard from "@/components/ChallengeOverviewCard";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import PendingChallengePage from "./components/pending-challenges/PendingChallengePage";
import ParticipantStatus from "./components/ParticipantStatus";
import GoalCompletionTable from "@/components/goal-completion-table";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: string }>;
}) {
  await getValidSession();
  const { challengeId: challengeIdStr } = await params;
  const challengeId = Number(challengeIdStr);

  const [curParticipant, challengeDetail] = await Promise.all([
    client.GET("/challenges/{challengeId}/participants/me", {
      params: { path: { challengeId } },
    }),
    client.GET("/challenges/{challengeId}", {
      params: { path: { challengeId } },
    }),
  ]);

  if (curParticipant.error) {
    throw new Error("Could not find your participant details");
  }
  if (challengeDetail.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  const { challenge } = challengeDetail.data;
  return (
    <>
      <ChallengeOverviewCard challenge={challenge} />
      {challenge.status === "PENDING" ? (
        <PendingChallengePage
          challengeDetails={challengeDetail.data}
          curParticipantId={curParticipant.data.id}
        />
      ) : (
        <>
          <ParticipantStatus
            challengeId={challenge.id}
            participants={challengeDetail.data.participants}
            challengeIsPending={false}
          />
          <GoalCompletionTable
            challengeId={challengeId}
            canModifyGoalCompletions={challenge.status === "IN_PROGRESS"}
            curParticipantId={curParticipant.data.id}
          />
        </>
      )}
    </>
  );
}
