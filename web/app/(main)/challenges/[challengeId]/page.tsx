import ChallengeOverviewCard from "@/components/ChallengeOverviewCard";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import GoalCompletionTable from "./components/GoalCompletionTable";
import PendingChallengePage from "./components/pending-challenges/PendingChallengePage";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

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
          curParticipantId={curParticipant.data.participantId}
        />
      ) : (
        <GoalCompletionTable
          challengeId={challengeId}
          challengeInProgress={challenge.status === "IN_PROGRESS"}
          curParticipantId={curParticipant.data.participantId}
        />
      )}
    </>
  );
}
