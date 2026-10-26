import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { NonPendingChallenge } from "./components/non-pending-challenge";
import { PendingChallengePage } from "./components/pending-challenge";

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
    <div className="grid grid-cols-1 gap-y-6">
      {challenge.status === "PENDING" ? (
        <PendingChallengePage
          challengeDetails={challengeDetail.data}
          curParticipantId={curParticipant.data.id}
        />
      ) : (
        <NonPendingChallenge
          challengeDetails={challengeDetail.data}
          curParticipantId={curParticipant.data.id}
        />
      )}
    </div>
  );
}
