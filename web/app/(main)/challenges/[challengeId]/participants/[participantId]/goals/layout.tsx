import { Button } from "@/components/ui/button";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { getParticipantDisplayName } from "@/lib/participant-utils";
import Link from "next/link";

export default async function Layout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ challengeId: number; participantId: number }>;
}) {
  await getValidSession();

  const { challengeId, participantId } = await params;

  const { data: participant, error } = await client.GET(
    "/challenges/{challengeId}/participants/{participantId}",
    {
      params: { path: { challengeId, participantId } },
    }
  );

  return (
    <div>
      {error ? (
        <p>Something went wrong. Could not get participant info.</p>
      ) : (
        <p>{`Goals for ${getParticipantDisplayName(participant)}`}</p>
      )}
      <div>{children}</div>
      <Button
        render={<Link href={`/challenges/${challengeId}`} />}
        nativeButton={false}
      >
        Return to challenge
      </Button>
    </div>
  );
}
