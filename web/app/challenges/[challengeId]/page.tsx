import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import ParticipantStatus from "./components/ParticipantStatus";
import { CircleCheck, Circle } from "lucide-react";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

  const [challengeDetailRes, goalTableRes] = await Promise.all([
    client.GET("/challenges/{challengeId}", {
      params: { path: { challengeId } },
    }),
    client.GET("/challenges/{challengeId}/goals/completions", {
      params: { path: { challengeId } },
    }),
  ]);

  if (challengeDetailRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  if (goalTableRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  const { challenge, participants } = challengeDetailRes.data;
  const { goalCompletionRowResponses, goalDefinitionResponse } =
    goalTableRes.data;

  return (
    <>
      <h1>{challenge.name}</h1>
      {challenge.durationDays && <p>{challenge.durationDays} days</p>}
      <p>Challenge status: {challenge.status}</p>
      {challenge.starDate && <p>Start date: {challenge.starDate}</p>}
      <ParticipantStatus participants={participants} />
      <h2>Your goals</h2>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Date</TableHead>
            {goalDefinitionResponse?.map((def) => (
              <TableHead key={def.id}>{def.name}</TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>
          {goalCompletionRowResponses.map((row) => (
            <TableRow key={row.date}>
              <TableCell>{row.date}</TableCell>
              {goalDefinitionResponse.map((goal) => (
                <TableCell key={goal.id}>
                  {row.completionsPerGoal[goal.id] ? (
                    <CircleCheck />
                  ) : (
                    <Circle />
                  )}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </>
  );
}
