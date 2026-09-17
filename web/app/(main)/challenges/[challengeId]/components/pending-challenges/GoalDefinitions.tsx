import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { client } from "@/lib/api-client";
import { MoreHorizontalIcon } from "lucide-react";
import GoalDefinitionActionMenu from "./GoalDefinitionActionMenu";

type GoalDefinitionsProps = {
  challengeId: number;
};

export default async function GoalDefinitions({
  challengeId,
}: GoalDefinitionsProps) {
  const { data, error } = await client.GET("/challenges/{challengeId}/goals", {
    params: { path: { challengeId } },
  });

  if (error) {
    throw new Error(`Unable to get goals for challenge ${challengeId}`);
  }

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Goal</TableHead>
          <TableHead className="text-right">Action</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {data.map((goal) => (
          <TableRow key={goal.id}>
            <TableCell>{goal.name}</TableCell>
            <TableCell className="flex justify-end">
              <GoalDefinitionActionMenu
                challengeId={challengeId}
                goalDefinitionId={goal.id}
              />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
