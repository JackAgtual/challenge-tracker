import { Button } from "@/components/ui/button";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { getParticipantDisplayName } from "@/lib/participant-utils";
import Link from "next/link";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number; participantId: number }>;
}) {
  const { challengeId, participantId } = await params;

  await getValidSession();

  const reqParams = {
    path: {
      challengeId,
      participantId,
    },
  };

  const [participant, goalDefinitions] = await Promise.all([
    client.GET("/challenges/{challengeId}/participants/{participantId}", {
      params: reqParams,
    }),
    client.GET("/challenges/{challengeId}/participants/{participantId}/goals", {
      params: reqParams,
    }),
  ]);

  return (
    <div>
      {participant.error ? (
        <p>Something went wrong. Could not get participant info.</p>
      ) : (
        <p>{`Goals for ${getParticipantDisplayName(participant.data)}`}</p>
      )}
      {goalDefinitions.error ? (
        <div>Something went wrong. Could not get goal definitions.</div>
      ) : (
        <>
          {goalDefinitions.data.length === 0 ? (
            <p>No goal definitions yet</p>
          ) : (
            <ul>
              {goalDefinitions.data.map((goalDefinition) => (
                <li key={goalDefinition.id}>{goalDefinition.name}</li>
              ))}
            </ul>
          )}
        </>
      )}
      <Button
        render={<Link href={`/challenges/${challengeId}`} />}
        nativeButton={false}
      >
        Return to challenge
      </Button>
    </div>
  );
}
