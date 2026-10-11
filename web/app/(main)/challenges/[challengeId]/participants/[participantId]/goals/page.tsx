import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: string; participantId: string }>;
}) {
  const { challengeId: challengeIdStr, participantId: participantIdStr } =
    await params;
  const challengeId = Number(challengeIdStr);
  const participantId = Number(participantIdStr);

  await getValidSession();

  const goalDefinitions = await client.GET(
    "/challenges/{challengeId}/participants/{participantId}/goals",
    {
      params: {
        path: {
          challengeId,
          participantId,
        },
      },
    }
  );

  return (
    <>
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
    </>
  );
}
